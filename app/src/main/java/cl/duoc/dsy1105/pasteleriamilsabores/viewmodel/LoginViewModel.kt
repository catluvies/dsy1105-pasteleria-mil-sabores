package cl.duoc.dsy1105.pasteleriamilsabores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.dsy1105.pasteleriamilsabores.model.User
import cl.duoc.dsy1105.pasteleriamilsabores.repository.UserRepository
import cl.duoc.dsy1105.pasteleriamilsabores.utils.HashingUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado de la UI para la pantalla de Login
data class LoginUiState(
    val email: String = "",
    val password: String = ""
)

// Eventos para notificar a la UI (éxito o error)
sealed class LoginEvent {
    data class Success(val user: User) : LoginEvent()
    data class Error(val message: String) : LoginEvent()
}

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _loginEvent = MutableSharedFlow<LoginEvent>()
    val loginEvent = _loginEvent.asSharedFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun login() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.email.isBlank() || state.password.isBlank()) {
                _loginEvent.emit(LoginEvent.Error("Email y contraseña son obligatorios."))
                return@launch
            }

            val storedCredentials = userRepository.findCredentialsByEmail(state.email)
            if (storedCredentials == null) {
                _loginEvent.emit(LoginEvent.Error("Usuario o contraseña incorrectos."))
                return@launch
            }

            val passwordAttemptHash = HashingUtils.hashPassword(state.password)
            if (passwordAttemptHash == storedCredentials.passwordHash) {
                // Éxito: los hashes coinciden
                val user = userRepository.findUserByEmail(state.email)
                if (user != null) {
                    _loginEvent.emit(LoginEvent.Success(user))
                } else {
                    // Esto no debería pasar si los datos son consistentes
                    _loginEvent.emit(LoginEvent.Error("Error interno: no se encontró el perfil del usuario."))
                }
            } else {
                // Fracaso: los hashes no coinciden
                _loginEvent.emit(LoginEvent.Error("Usuario o contraseña incorrectos."))
            }
        }
    }
}
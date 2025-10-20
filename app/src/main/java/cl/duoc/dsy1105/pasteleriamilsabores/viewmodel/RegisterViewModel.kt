package cl.duoc.dsy1105.pasteleriamilsabores.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.dsy1105.pasteleriamilsabores.model.User
import cl.duoc.dsy1105.pasteleriamilsabores.model.UserCredentials
import cl.duoc.dsy1105.pasteleriamilsabores.repository.UserRepository
import cl.duoc.dsy1105.pasteleriamilsabores.utils.HashingUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// La clase 'RegistrationEvent' ha sido ELIMINADA de este archivo.

class RegisterViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _registrationEvent = MutableSharedFlow<RegistrationEvent>()
    val registrationEvent = _registrationEvent.asSharedFlow()

    fun onFullNameChange(name: String) {
        _uiState.update { it.copy(fullName = name, errors = it.errors.copy(fullName = null)) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errors = it.errors.copy(email = null)) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errors = it.errors.copy(password = null)) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, errors = it.errors.copy(confirmPassword = null)) }
    }

    private fun validateForm(): Boolean {
        val state = _uiState.value
        val errors = RegisterFormErrors(
            fullName = if (state.fullName.isBlank()) "Campo obligatorio" else null,
            email = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) "Correo inválido" else null,
            password = if (state.password.length < 6) "Debe tener al menos 6 caracteres" else null,
            confirmPassword = if (state.password != state.confirmPassword) "Las contraseñas no coinciden" else null
        )
        _uiState.update { it.copy(errors = errors) }
        return errors.fullName == null && errors.email == null && errors.password == null && errors.confirmPassword == null
    }

    fun register() {
        if (validateForm()) {
            viewModelScope.launch {
                val state = _uiState.value
                if (userRepository.findUserByEmail(state.email) != null) {
                    _registrationEvent.emit(RegistrationEvent.Error("El correo electrónico ya está registrado."))
                    return@launch
                }
                val newUser = User(
                    id = (1..1000).random(),
                    fullName = state.fullName,
                    email = state.email,
                    address = "",
                    phone = ""
                )
                val passwordHash = HashingUtils.hashPassword(state.password)
                val newCredentials = UserCredentials(email = state.email, passwordHash = passwordHash)
                userRepository.registerUser(newUser, newCredentials)
                Log.d("RegisterViewModel", "Usuario Registrado: ${newUser.fullName}")
                Log.d("RegisterViewModel", "Hash de Contraseña: $passwordHash")
                _registrationEvent.emit(RegistrationEvent.Success(newUser))
            }
        }
    }
}
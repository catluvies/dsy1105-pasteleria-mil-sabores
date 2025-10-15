package cl.duoc.dsy1105.pasteleriamilsabores.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.dsy1105.pasteleriamilsabores.model.User
import cl.duoc.dsy1105.pasteleriamilsabores.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Al igual que RegisterViewModel, ahora depende de una instancia de UserRepository.
class UserSessionViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _currentUserState = MutableStateFlow<User?>(null)
    val currentUserState = _currentUserState.asStateFlow()

    fun onLoginSuccess(user: User) {
        _currentUserState.update { user }
    }

    fun updateUserData(address: String, phone: String) {
        _currentUserState.value?.let { currentUser ->
            val updatedUser = currentUser.copy(address = address, phone = phone)
            // Llamamos a la función a través de la instancia 'userRepository'
            userRepository.updateUser(updatedUser)
            _currentUserState.update { updatedUser }
        }
    }

    fun logout() {
        _currentUserState.update { null }
    }
}
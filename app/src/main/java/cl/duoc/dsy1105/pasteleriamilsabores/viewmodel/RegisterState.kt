package cl.duoc.dsy1105.pasteleriamilsabores.viewmodel

import cl.duoc.dsy1105.pasteleriamilsabores.model.User

// ================== CLASE AÑADIDA AQUÍ ==================
// Eventos que la UI puede recibir del ViewModel para acciones de una sola vez.
sealed class RegistrationEvent {
    data class Success(val user: User) : RegistrationEvent()
    data class Error(val message: String) : RegistrationEvent()
}
// =======================================================

// Modelo que almacena posibles errores individuales del formulario
data class RegisterFormErrors(
    val fullName: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null
)

// Modelo principal que representa el estado del formulario del usuario
data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val errors: RegisterFormErrors = RegisterFormErrors()
)
package cl.duoc.dsy1105.pasteleriamilsabores.model

data class UserCredentials(
    val email: String,
    val passwordHash: String // Guardaremos el HASH, no la contraseña
)
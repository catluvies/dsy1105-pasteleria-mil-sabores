package cl.duoc.dsy1105.pasteleriamilsabores.model

data class User(
    val id: Int,
    val fullName: String,
    val email: String,
    var address: String,
    var phone: String
)
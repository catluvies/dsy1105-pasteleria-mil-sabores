package cl.duoc.dsy1105.pasteleriamilsabores.repository

import cl.duoc.dsy1105.pasteleriamilsabores.model.User
import cl.duoc.dsy1105.pasteleriamilsabores.model.UserCredentials
import cl.duoc.dsy1105.pasteleriamilsabores.utils.HashingUtils

/**
 * Gestiona los datos de los usuarios y sus credenciales.
 * En esta versión simulada, utiliza listas en memoria.
 * Al cambiar a 'class', permitimos que se creen instancias,
 * lo que mejora la testeabilidad y la flexibilidad de la arquitectura.
 */
class UserRepository {

    // Estas listas ahora pertenecen a una instancia de UserRepository,
    // no son globales para toda la app.
    private val users = mutableListOf<User>()
    private val credentials = mutableListOf<UserCredentials>()

    init {
        val adminUser = User(
            id = 999,
            fullName = "Administrador",
            email = "admin@pasteleria.cl",
            address = "",
            phone = "",
            isAdmin = true

        )
        val adminCredentials = UserCredentials(
            email = "admin@pasteleria.cl",
            passwordHash = HashingUtils.hashPassword("admin123")
        )

        users.add(adminUser)
        credentials.add(adminCredentials)

    }



    fun findUserByEmail(email: String): User? {
        return users.find { it.email.equals(email, ignoreCase = true) }
    }

    fun findCredentialsByEmail(email: String): UserCredentials? {
        return credentials.find { it.email.equals(email, ignoreCase = true) }
    }

    fun registerUser(user: User, userCredentials: UserCredentials) {
        // Para evitar duplicados en la simulación
        if (findUserByEmail(user.email) == null) {
            users.add(user)
            credentials.add(userCredentials)
        }
    }

    fun updateUser(updatedUser: User) {
        val index = users.indexOfFirst { it.id == updatedUser.id }
        if (index != -1) {
            users[index] = updatedUser
        }
    }
}
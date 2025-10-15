package cl.duoc.dsy1105.pasteleriamilsabores.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.dsy1105.pasteleriamilsabores.repository.UserRepository
import cl.duoc.dsy1105.pasteleriamilsabores.ui.screens.CatalogScreen
import cl.duoc.dsy1105.pasteleriamilsabores.ui.screens.LoginScreen
import cl.duoc.dsy1105.pasteleriamilsabores.ui.screens.RegisterScreen
import cl.duoc.dsy1105.pasteleriamilsabores.ui.screens.UserProfileScreen
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.LoginViewModel
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.RegisterViewModel
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.UserSessionViewModel

sealed class AppScreen(val route: String) {
    object CatalogScreen : AppScreen("catalog")
    object LoginScreen : AppScreen("login")
    object RegisterScreen : AppScreen("register")
    object UserProfileScreen : AppScreen("user_profile")
}

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(userRepository) as T
            // ================== CORRECCIÓN DEFINITIVA AQUÍ ==================
            modelClass.isAssignableFrom(UserSessionViewModel::class.java) -> UserSessionViewModel(userRepository) as T
            // ================================================================
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(userRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val userRepository = remember { UserRepository() }
    val viewModelFactory = ViewModelFactory(userRepository)
    val userSessionViewModel: UserSessionViewModel = viewModel(factory = viewModelFactory)

    NavHost(
        navController = navController,
        startDestination = AppScreen.CatalogScreen.route
    ) {
        composable(route = AppScreen.CatalogScreen.route) {
            val currentUser by userSessionViewModel.currentUserState.collectAsStateWithLifecycle()
            CatalogScreen(
                onProfileClick = {
                    if (currentUser == null) navController.navigate(AppScreen.LoginScreen.route)
                    else navController.navigate(AppScreen.UserProfileScreen.route)
                }
            )
        }

        composable(route = AppScreen.LoginScreen.route) {
            val loginViewModel: LoginViewModel = viewModel(factory = viewModelFactory)
            LoginScreen(
                loginViewModel = loginViewModel,
                onRegisterClick = { navController.navigate(AppScreen.RegisterScreen.route) },
                onLoginSuccess = { loggedInUser ->
                    userSessionViewModel.onLoginSuccess(loggedInUser)
                    navController.navigate(AppScreen.UserProfileScreen.route) {
                        popUpTo(AppScreen.LoginScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = AppScreen.RegisterScreen.route) {
            val registerViewModel: RegisterViewModel = viewModel(factory = viewModelFactory)
            RegisterScreen(
                registerViewModel = registerViewModel,
                onLoginClick = { navController.popBackStack() },
                onRegisterSuccess = { newUser ->
                    userSessionViewModel.onLoginSuccess(newUser)
                    navController.navigate(AppScreen.UserProfileScreen.route) {
                        popUpTo(AppScreen.LoginScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = AppScreen.UserProfileScreen.route) {
            UserProfileScreen(
                userSessionViewModel = userSessionViewModel,
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    userSessionViewModel.logout()
                    navController.navigate(AppScreen.CatalogScreen.route) {
                        popUpTo(AppScreen.CatalogScreen.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
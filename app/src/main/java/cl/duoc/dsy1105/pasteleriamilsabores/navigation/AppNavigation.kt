package cl.duoc.dsy1105.pasteleriamilsabores.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.dsy1105.pasteleriamilsabores.data.AppDatabase
import cl.duoc.dsy1105.pasteleriamilsabores.data.sampleProductList
import cl.duoc.dsy1105.pasteleriamilsabores.repository.UserRepository
import cl.duoc.dsy1105.pasteleriamilsabores.ui.screens.CatalogScreen
import cl.duoc.dsy1105.pasteleriamilsabores.ui.screens.CarritoScreen
import cl.duoc.dsy1105.pasteleriamilsabores.ui.screens.LoginScreen
import cl.duoc.dsy1105.pasteleriamilsabores.ui.screens.ProductDetailsScreen
import cl.duoc.dsy1105.pasteleriamilsabores.ui.screens.RegisterScreen
import cl.duoc.dsy1105.pasteleriamilsabores.ui.screens.UserProfileScreen
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.CartViewModel
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.LoginViewModel
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.RegisterViewModel
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.UserSessionViewModel

sealed class AppScreen(val route: String) {
    data object CatalogScreen : AppScreen("catalog")
    data object LoginScreen : AppScreen("login")
    data object RegisterScreen : AppScreen("register")
    data object UserProfileScreen : AppScreen("user_profile")
    data object CartScreen : AppScreen("cart")
    data object ProductDetail : AppScreen("product/{id}") {
        fun createRoute(id: Int) = "product/$id"
    }
}

@Suppress("UNCHECKED_CAST")
class UserVMFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(userRepository) as T
            modelClass.isAssignableFrom(UserSessionViewModel::class.java) -> UserSessionViewModel(userRepository) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(userRepository) as T
            else -> error("Unknown ViewModel class: ${modelClass.name}")
        }
}

class CartVMFactory(private val dao: cl.duoc.dsy1105.pasteleriamilsabores.data.CartDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) return CartViewModel(dao) as T
        error("Unknown ViewModel class: ${modelClass.name}")
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // User session VMs
    val userRepository = remember { UserRepository() }
    val userVmFactory = remember { UserVMFactory(userRepository) }
    val userSessionViewModel: UserSessionViewModel = viewModel(factory = userVmFactory)

    // Cart VM (compartido)
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val cartViewModel: CartViewModel = viewModel(factory = CartVMFactory(db.cartDao()))

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
                },
                onCartClick = { navController.navigate(AppScreen.CartScreen.route) },
                cartViewModel = cartViewModel,
                onProductClick = { id ->
                    navController.navigate(AppScreen.ProductDetail.createRoute(id))
                }
            )
        }

        composable(route = AppScreen.ProductDetail.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val product = sampleProductList.find { it.id == id }
            if (product != null) {
                ProductDetailsScreen(
                    product = product,
                    onBack = { navController.popBackStack() },
                    onCartClick = { navController.navigate(AppScreen.CartScreen.route) }
                )
            } else {
                // Si no se encuentra, volvemos
                navController.popBackStack()
            }
        }

        composable(route = AppScreen.CartScreen.route) {
            CarritoScreen(
                onBack = { navController.popBackStack() },
                viewModel = cartViewModel
            )
        }

        composable(route = AppScreen.LoginScreen.route) {
            val loginViewModel: LoginViewModel = viewModel(factory = userVmFactory)
            LoginScreen(
                loginViewModel = loginViewModel,
                onRegisterClick = { navController.navigate(AppScreen.RegisterScreen.route) },
                onLoginSuccess = { user ->
                    userSessionViewModel.onLoginSuccess(user)
                    navController.navigate(AppScreen.UserProfileScreen.route) {
                        popUpTo(AppScreen.LoginScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = AppScreen.RegisterScreen.route) {
            val registerViewModel: RegisterViewModel = viewModel(factory = userVmFactory)
            RegisterScreen(
                registerViewModel = registerViewModel,
                onLoginClick = { navController.popBackStack() },
                onRegisterSuccess = { user ->
                    userSessionViewModel.onLoginSuccess(user)
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

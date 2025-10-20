package cl.duoc.dsy1105.pasteleriamilsabores.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import cl.duoc.dsy1105.pasteleriamilsabores.repository.ProductRepository
import cl.duoc.dsy1105.pasteleriamilsabores.repository.UserRepository
import cl.duoc.dsy1105.pasteleriamilsabores.ui.screens.*
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.*

sealed class AppScreen(val route: String) {
    data object CatalogScreen : AppScreen("catalog")
    data object LoginScreen : AppScreen("login")
    data object RegisterScreen : AppScreen("register")
    data object UserProfileScreen : AppScreen("user_profile")
    data object CartScreen : AppScreen("cart")
    data object ProductDetail : AppScreen("product/{id}") {
        fun createRoute(id: Int) = "product/$id"
    }
    data object AdminPanel : AppScreen("admin_panel")
    data object ProductManagement : AppScreen("product_management")
    data object AddProduct : AppScreen("add_product")
    data object EditProduct : AppScreen("edit_product/{id}") {
        fun createRoute(id: Int) = "edit_product/$id"
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

class ProductVMFactory(private val productRepository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) return ProductViewModel(productRepository) as T
        error("Unknown ViewModel class: ${modelClass.name}")
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val userRepository = remember { UserRepository() }
    val userVmFactory = remember { UserVMFactory(userRepository) }
    val userSessionViewModel: UserSessionViewModel = viewModel(factory = userVmFactory)

    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }

    val cartViewModel: CartViewModel = viewModel(factory = CartVMFactory(db.cartDao()))

    val productRepository = remember { ProductRepository(db.productDao()) }
    val productVmFactory = remember { ProductVMFactory(productRepository) }
    val productViewModel: ProductViewModel = viewModel(factory = productVmFactory)

    LaunchedEffect(Unit) { productViewModel.seedIfEmpty(sampleProductList) }

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
                onProductClick = { id -> navController.navigate(AppScreen.ProductDetail.createRoute(id)) },
                productViewModel = productViewModel
            )
        }

        composable(route = AppScreen.ProductDetail.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val products by productViewModel.products.collectAsStateWithLifecycle()
            val product = products.find { it.id == id }
            if (product != null) {
                ProductDetailsScreen(
                    product = product,
                    onBack = { navController.popBackStack() },
                    onCartClick = { navController.navigate(AppScreen.CartScreen.route) },
                    cartViewModel = cartViewModel
                )
            } else {
                navController.popBackStack()
            }
        }

        composable(route = AppScreen.CartScreen.route) {
            CarritoScreen(
                onBack = { navController.popBackStack() },
                viewModel = cartViewModel,
                productViewModel = productViewModel   // ✅ PASAMOS productos desde Room
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
            val currentUser by userSessionViewModel.currentUserState.collectAsStateWithLifecycle()
            UserProfileScreen(
                userSessionViewModel = userSessionViewModel,
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    userSessionViewModel.logout()
                    navController.navigate(AppScreen.CatalogScreen.route) {
                        popUpTo(AppScreen.CatalogScreen.route) { inclusive = true }
                    }
                },
                onAdminPanelClick = {
                    if (currentUser?.isAdmin == true) {
                        navController.navigate(AppScreen.AdminPanel.route)
                    }
                }
            )
        }

        composable(route = AppScreen.AdminPanel.route) {
            AdminPanelScreen(
                onNavigateBack = { navController.popBackStack() },
                onManageProducts = { navController.navigate(AppScreen.ProductManagement.route) },
                onViewCatalog = {
                    navController.navigate(AppScreen.CatalogScreen.route) {
                        popUpTo(AppScreen.CatalogScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = AppScreen.ProductManagement.route) {
            ProductManagementScreen(
                productViewModel = productViewModel,
                onNavigateBack = { navController.popBackStack() },
                onAddProduct = { navController.navigate(AppScreen.AddProduct.route) },
                onEditProduct = { product ->
                    navController.navigate(AppScreen.EditProduct.createRoute(product.id))
                }
            )
        }

        composable(route = AppScreen.AddProduct.route) {
            AddEditProductScreen(
                productViewModel = productViewModel,
                existingProduct = null,
                onNavigateBack = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(route = AppScreen.EditProduct.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val products by productViewModel.products.collectAsStateWithLifecycle()
            val product = products.find { it.id == id }
            if (product != null) {
                AddEditProductScreen(
                    productViewModel = productViewModel,
                    existingProduct = product,
                    onNavigateBack = { navController.popBackStack() },
                    onSaveSuccess = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }
    }
}

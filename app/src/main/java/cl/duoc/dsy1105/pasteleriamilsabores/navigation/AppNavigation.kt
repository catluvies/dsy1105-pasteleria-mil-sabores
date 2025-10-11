package cl.duoc.dsy1105.pasteleriamilsabores.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.dsy1105.pasteleriamilsabores.ui.screens.LoginScreen
import cl.duoc.dsy1105.pasteleriamilsabores.ui.screens.RegisterScreen

sealed class AppScreen(val route: String) {
    object LoginScreen : AppScreen("login")
    object RegisterScreen : AppScreen("register")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    //significa que vamos a ver login por el momento como pantalla de inicio
    NavHost(
        navController = navController,
        startDestination = AppScreen.LoginScreen.route
    ) {

        //por ahora solo tiene uno de sus dos botones funcionando
        composable(route = AppScreen.LoginScreen.route) {
            LoginScreen(
                onRegisterClick = {
                    // Cuando se haga clic, navegamos a la ruta de Registro.
                    navController.navigate(AppScreen.RegisterScreen.route)
                }
            )
        }

        //por ahora solo tiene uno de sus dos botones funcionando x2
        composable(route = AppScreen.RegisterScreen.route) {
            RegisterScreen(
                onLoginClick = {
                    // Cuando se haga clic, navegamos de vuelta a la ruta de Login.
                    navController.navigate(AppScreen.LoginScreen.route)
                }
            )
        }
    }
}
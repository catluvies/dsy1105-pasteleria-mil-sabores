package cl.duoc.dsy1105.pasteleriamilsabores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import cl.duoc.dsy1105.pasteleriamilsabores.navigation.AppNavigation
import cl.duoc.dsy1105.pasteleriamilsabores.ui.theme.PasteleriaMilSaboresTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite que el contenido se dibuje detrás de las barras del sistema
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            var darkTheme by remember { mutableStateOf(false) }

            PasteleriaMilSaboresTheme(darkTheme = darkTheme) {
                // Configurar colores de la barra de estado
                val statusBarColor = MaterialTheme.colorScheme.surface.toArgb()
                val navigationBarColor = MaterialTheme.colorScheme.surface.toArgb()

                SideEffect {
                    window.statusBarColor = statusBarColor
                    window.navigationBarColor = navigationBarColor

                    // Configurar iconos de barra de estado
                    WindowCompat.getInsetsController(window, window.decorView).apply {
                        isAppearanceLightStatusBars = !darkTheme  // Iconos oscuros en light mode
                        isAppearanceLightNavigationBars = !darkTheme  // Iconos oscuros en light mode
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        darkTheme = darkTheme,
                        onThemeChange = { darkTheme = it }
                    )
                }
            }
        }
    }
}
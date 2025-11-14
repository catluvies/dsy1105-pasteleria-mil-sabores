package cl.duoc.dsy1105.pasteleriamilsabores.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun PasteleriaMilSaboresTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = ChocolateClaro,                 // Marrón claro para botones
            onPrimary = Color.White,                  // Texto blanco en botones
            secondary = CremaOscuro,                   // Crema para secundarios
            onSecondary = ChocolateOscuro,            // Texto oscuro en secundarios
            tertiary = RosaAccento,                   // Rosa para acentos
            onTertiary = Color.White,
            background = Color(0xFF1C1412),           // Fondo marrón muy oscuro
            onBackground = Color(0xFFF5E6D3),         // Texto crema claro
            surface = Color(0xFF2C2420),              // Superficie marrón oscuro
            onSurface = Color(0xFFF5E6D3),            // Texto crema claro
            surfaceVariant = Color(0xFF3D342F),       // Variante más clara
            onSurfaceVariant = Color(0xFFD4C4B4),     // Texto medio
            error = Color(0xFFCF6679),                // Rojo pastel
            onError = Color.White
        )
    } else {
        lightColorScheme(
            primary = ChocolateOscuro,                // Marrón chocolate para botones
            onPrimary = Color.White,                  // Texto blanco en botones
            secondary = ChocolateClaro,               // Marrón claro para secundarios
            onSecondary = Color.White,                // Texto blanco
            tertiary = RosaAccento,                   // Rosa para acentos
            onTertiary = Color.White,
            background = CremaPastel,                 // Fondo crema suave
            onBackground = ChocolateOscuro,           // Texto marrón oscuro
            surface = Color.White,                    // Superficies blancas
            onSurface = ChocolateOscuro,              // Texto marrón oscuro
            surfaceVariant = CremaOscuro,             // Variante crema
            onSurfaceVariant = ChocolateClaro,        // Texto marrón claro
            error = Color(0xFFD32F2F),                // Rojo fuerte
            onError = Color.White
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PasteleriaTypography,
        content = content
    )
}
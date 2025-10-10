package cl.duoc.dsy1105.pasteleriamilsabores.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun PasteleriaMilSaboresTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = lightColorScheme(
        primary = RosaSuave,
        onPrimary = Color.White,
        secondary = Chocolate,
        onSecondary = Color.White,
        background = CremaPastel,
        onBackground = MarronOscuro,
        surface = CremaPastel,
        onSurface = MarronOscuro,
        onSurfaceVariant = GrisClaro
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PasteleriaTypography,
        content = content
    )
}
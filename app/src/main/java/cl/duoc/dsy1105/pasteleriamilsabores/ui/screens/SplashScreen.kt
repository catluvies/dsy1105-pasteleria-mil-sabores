package cl.duoc.dsy1105.pasteleriamilsabores.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.dsy1105.pasteleriamilsabores.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToCatalog: () -> Unit
) {
    val scale = remember { Animatable(0.5f) }
    val alphaIndicator = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            )
        )
        alphaIndicator.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300)
        )
        delay(2000)
        onNavigateToCatalog()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF5E1),
                        Color(0xFFFFE4E1)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.scale(scale.value)
        ) {
            // LOGO (sin texto)
            Image(
                painter = painterResource(id = R.drawable.poro_galleta),
                contentDescription = "Logo",
                modifier = Modifier.size(280.dp)
            )

            // TÍTULO EN CURSIVA
            Text(
                text = "Pastelería Mil Sabores",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Cursive,
                color = Color(0xFF8B4513),
                textAlign = TextAlign.Center,
                letterSpacing = 1.sp
            )

            // BIENVENIDA
            Text(
                text = "¡Bienvenido/a!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFFFC0CB),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // INDICADOR DE CARGA
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.alpha(alphaIndicator.value)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = Color(0xFFFFC0CB),
                    strokeWidth = 4.dp
                )
                Text(
                    text = "Cargando...",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF8B4513).copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
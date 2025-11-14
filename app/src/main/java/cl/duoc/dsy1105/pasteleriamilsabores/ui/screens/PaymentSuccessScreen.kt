package cl.duoc.dsy1105.pasteleriamilsabores.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.dsy1105.pasteleriamilsabores.R
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.CartViewModel

@Composable
fun PaymentSuccessScreen(
    onBackToCatalog: () -> Unit,
    cartViewModel: CartViewModel
) {
    // Limpiar el carrito al entrar a esta pantalla
    LaunchedEffect(Unit) {
        cartViewModel.clearCart()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // LOGO WEBPAY ARRIBA - MÁS ABAJO
        Image(
            painter = painterResource(id = R.drawable.logo_webpay),
            contentDescription = "WebPay",
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(top = 32.dp, bottom = 12.dp), // Aumentado de 16dp a 32dp
            contentScale = ContentScale.Fit
        )

        // IMAGEN DE CONFIRMACIÓN - USA TODO EL ESPACIO DISPONIBLE
        Image(
            painter = painterResource(id = R.drawable.imagen_fake_pago),
            contentDescription = "Comprobante de pago exitoso",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentScale = ContentScale.Fit
        )

        // FOOTER CON FONDO PARA QUE SE VEA SOBRE LA BARRA DEL CELULAR
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 8.dp // Sombra para que destaque
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp, bottom = 20.dp), // Más padding abajo
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onBackToCatalog,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        "Volver al Catálogo",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Text(
                    "Gracias por tu preferencia 💖",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
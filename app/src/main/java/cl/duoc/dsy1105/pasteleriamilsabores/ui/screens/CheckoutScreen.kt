package cl.duoc.dsy1105.pasteleriamilsabores.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.dsy1105.pasteleriamilsabores.R
import cl.duoc.dsy1105.pasteleriamilsabores.utils.Formatters
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.CartViewModel
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.ProductViewModel
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.UserSessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBack: () -> Unit,
    onPaymentSuccess: () -> Unit,
    cartViewModel: CartViewModel,
    productViewModel: ProductViewModel,
    userSessionViewModel: UserSessionViewModel
) {
    val colors = MaterialTheme.colorScheme
    val currentUser by userSessionViewModel.currentUserState.collectAsStateWithLifecycle()

    val cartItems by cartViewModel.cartItems.collectAsStateWithLifecycle()
    val products by productViewModel.products.collectAsStateWithLifecycle()

    val uiItems = cartItems.mapNotNull { ci ->
        products.find { it.id == ci.productId }?.let { p ->
            Pair(p, ci.quantity)
        }
    }

    val total = uiItems.sumOf { it.first.price * it.second }
    val itemCount = cartItems.sumOf { it.quantity }

    var nombre by remember { mutableStateOf(currentUser?.fullName ?: "") }
    var direccion by remember { mutableStateOf(currentUser?.address ?: "") }
    var telefono by remember { mutableStateOf(currentUser?.phone ?: "") }
    var comentarios by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Datos de Envío") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // RESUMEN DEL PEDIDO
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = colors.primaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Resumen del Pedido",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = colors.onSurface
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "$itemCount producto${if (itemCount != 1) "s" else ""}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = colors.onSurfaceVariant
                        )
                        Text(
                            Formatters.clPriceFormatter.format(total),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = colors.primary
                        )
                    }
                }
            }

            HorizontalDivider()

            // FORMULARIO
            Text(
                "Información de Entrega",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección de Envío") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 3
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono de Contacto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                prefix = { Text("+56 ") }
            )

            OutlinedTextField(
                value = comentarios,
                onValueChange = { comentarios = it },
                label = { Text("Comentarios adicionales (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                placeholder = { Text("Ej: Tocar timbre, dejar en portería, etc.") }
            )

            HorizontalDivider()


            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = colors.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "💳 Método de Pago",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Text(
                                "Pago seguro con tarjeta de débito o crédito",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.onSurfaceVariant
                            )
                        }

                        Image(
                            painter = painterResource(id = R.drawable.webpay_logo),
                            contentDescription = "WebPay",
                            modifier = Modifier
                                .size(80.dp)
                                .padding(8.dp),
                            contentScale = ContentScale.Fit
                        )

                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // BOTÓN CONFIRMAR
            Button(
                onClick = {
                    // Simular procesamiento de pago con tarjeta
                    onPaymentSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = nombre.isNotBlank() && direccion.isNotBlank() && telefono.isNotBlank()
            ) {
                Text(
                    "Pagar con Tarjeta",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Text(
                "Tu pago será procesado de forma segura. Recibirás confirmación por WhatsApp.",
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}
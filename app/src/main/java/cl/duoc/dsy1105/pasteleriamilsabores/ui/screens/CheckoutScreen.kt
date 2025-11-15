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

// Pantalla de checkout donde el usuario ingresa datos de envío antes de pagar
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

    // Juntamos los productos con sus cantidades del carrito
    val uiItems = cartItems.mapNotNull { ci ->
        products.find { it.id == ci.productId }?.let { p ->
            Pair(p, ci.quantity)
        }
    }

    val total = uiItems.sumOf { it.first.price * it.second }
    val itemCount = cartItems.sumOf { it.quantity }

    // Estados para los campos del formulario, precargamos con datos del usuario si existe
    var nombre by remember { mutableStateOf(currentUser?.fullName ?: "") }
    var direccion by remember { mutableStateOf(currentUser?.address ?: "") }
    var telefono by remember { mutableStateOf(currentUser?.phone ?: "") }
    var comentarios by remember { mutableStateOf("") }

    // Estados para guardar errores de validación
    var nombreError by remember { mutableStateOf<String?>(null) }
    var direccionError by remember { mutableStateOf<String?>(null) }
    var telefonoError by remember { mutableStateOf<String?>(null) }

    // Valida que el nombre tenga al menos 3 caracteres y solo letras
    fun validarNombre(valor: String): String? {
        return when {
            valor.isBlank() -> "El nombre es obligatorio"
            valor.trim().length < 3 -> "El nombre debe tener al menos 3 caracteres"
            !valor.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) -> "El nombre solo puede contener letras"
            else -> null
        }
    }

    // Valida que la dirección tenga al menos 10 caracteres
    fun validarDireccion(valor: String): String? {
        return when {
            valor.isBlank() -> "La dirección es obligatoria"
            valor.trim().length < 10 -> "La dirección debe ser más específica (mín. 10 caracteres)"
            else -> null
        }
    }

    // Valida que el teléfono tenga exactamente 9 dígitos (formato chileno)
    fun validarTelefono(valor: String): String? {
        val soloNumeros = valor.replace(" ", "").replace("-", "")
        return when {
            soloNumeros.isBlank() -> "El teléfono es obligatorio"
            !soloNumeros.matches(Regex("^[0-9]+$")) -> "El teléfono solo puede contener números"
            soloNumeros.length != 9 -> "El teléfono debe tener 9 dígitos"
            else -> null
        }
    }

    // Valida todos los campos y retorna true si todo está ok
    fun validarFormulario(): Boolean {
        nombreError = validarNombre(nombre)
        direccionError = validarDireccion(direccion)
        telefonoError = validarTelefono(telefono)
        return nombreError == null && direccionError == null && telefonoError == null
    }

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
            // Resumen del pedido
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

            Text(
                "Información de Entrega",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            // Campo nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    nombreError = validarNombre(it)
                },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = nombreError != null,
                supportingText = {
                    nombreError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            // Campo dirección
            OutlinedTextField(
                value = direccion,
                onValueChange = {
                    direccion = it
                    direccionError = validarDireccion(it)
                },
                label = { Text("Dirección de Envío") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 3,
                isError = direccionError != null,
                supportingText = {
                    direccionError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                placeholder = { Text("Ej: Av. Principal 123, Depto 45, Santiago") }
            )

            // Campo teléfono (solo permite números y máximo 9 dígitos)
            OutlinedTextField(
                value = telefono,
                onValueChange = {
                    val soloNumeros = it.filter { char -> char.isDigit() }
                    if (soloNumeros.length <= 9) {
                        telefono = soloNumeros
                        telefonoError = validarTelefono(soloNumeros)
                    }
                },
                label = { Text("Teléfono de Contacto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                prefix = { Text("+56 ") },
                isError = telefonoError != null,
                supportingText = {
                    telefonoError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    } ?: Text("Formato: 9 dígitos (ej: 987654321)")
                },
                placeholder = { Text("987654321") }
            )

            // Campo comentarios (opcional)
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

            // Método de pago
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

            // Botón de pago (valida antes de procesar)
            Button(
                onClick = {
                    if (validarFormulario()) {
                        onPaymentSuccess()
                    }
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

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
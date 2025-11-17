package cl.duoc.dsy1105.pasteleriamilsabores.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.dsy1105.pasteleriamilsabores.R
import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.CartViewModel
import cl.duoc.dsy1105.pasteleriamilsabores.utils.Formatters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    product: Product,
    onBack: () -> Unit,
    onCartClick: () -> Unit,
    onReviewsClick: () -> Unit,
    cartViewModel: CartViewModel
) {
    val colors = MaterialTheme.colorScheme
    val fallbackRes = R.drawable.torta_chocolate
    val safeResId = if (product.imageResId != 0) product.imageResId else fallbackRes

    val cartItems = cartViewModel.cartItems.collectAsStateWithLifecycle().value
    val cartCount = cartItems.sumOf { it.quantity }

    var qty by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    Box {
                        Surface(shape = CircleShape, color = colors.primary) {
                            IconButton(onClick = onCartClick) {
                                Icon(
                                    Icons.Filled.ShoppingCart,
                                    contentDescription = "Carrito",
                                    tint = colors.onPrimary
                                )
                            }
                        }
                        if (cartCount > 0) {
                            Badge(
                                containerColor = Color(0xFFFF3B30),
                                contentColor = Color.White,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-4).dp, y = 4.dp)
                            ) {
                                Text(
                                    text = if (cartCount > 99) "99+" else cartCount.toString(),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.surface
                )
            )
        },
        containerColor = colors.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colors.surfaceVariant
                )
            ) {
                Image(
                    painter = painterResource(id = safeResId),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                color = colors.onSurface
            )

            Text(
                text = Formatters.clPriceFormatter.format(product.price),
                style = MaterialTheme.typography.titleMedium,
                color = colors.onSurfaceVariant
            )

            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant
            )

            // Botón para ver reseñas de clientes
            OutlinedButton(
                onClick = onReviewsClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colors.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.RateReview,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver Reseñas de Clientes")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cantidad",
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.onSurface
                )

                Spacer(modifier = Modifier.weight(1f))

                FilledTonalButton(
                    onClick = { if (qty > 1) qty-- }
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Menos")
                }

                Text(
                    text = qty.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.onSurface
                )

                FilledTonalButton(
                    onClick = { if (qty < 99) qty++ }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Más")
                }
            }

            Button(
                onClick = {
                    cartViewModel.addProduct(product.id, qty)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar $qty al carrito")
            }
        }
    }
}
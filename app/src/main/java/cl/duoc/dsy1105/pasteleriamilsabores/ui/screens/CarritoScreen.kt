package cl.duoc.dsy1105.pasteleriamilsabores.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.dsy1105.pasteleriamilsabores.R
import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.CartViewModel
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.ProductViewModel
import java.text.NumberFormat
import java.util.Locale

private val clFormatter = NumberFormat.getCurrencyInstance(
    Locale.Builder().setLanguage("es").setRegion("CL").build()
)

private data class CartUiItem(val product: Product, val quantity: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    onBack: () -> Unit,
    viewModel: CartViewModel,
    productViewModel: ProductViewModel
) {
    val colors = MaterialTheme.colorScheme

    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val products by productViewModel.products.collectAsStateWithLifecycle()

    val uiItems: List<CartUiItem> = cartItems.mapNotNull { ci ->
        products.find { it.id == ci.productId }?.let { p -> CartUiItem(p, ci.quantity) }
    }

    val total = uiItems.sumOf { it.product.price * it.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (uiItems.isNotEmpty()) {
                        TextButton(
                            onClick = { viewModel.clearCart() }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Vaciar todo")
                        }
                        Spacer(Modifier.width(4.dp))
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(32.dp)
                ) {
                    Text(
                        text = "🛒",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(
                        text = "Tu carrito está vacío",
                        style = MaterialTheme.typography.titleLarge,
                        color = colors.onSurfaceVariant
                    )
                    Text(
                        text = "Agrega productos desde el catálogo",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.onSurfaceVariant
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiItems, key = { it.product.id }) { item ->
                        CartItemCard(
                            item = item,
                            onDecrease = { viewModel.decrease(item.product.id) },
                            onIncrease = { viewModel.increase(item.product.id) },
                            onRemove = { viewModel.removeProduct(item.product.id) }
                        )
                    }

                    item {
                        Spacer(Modifier.height(16.dp))
                    }
                }

                // FOOTER CON TOTAL
                Surface(
                    tonalElevation = 3.dp,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(
                                    "Total",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = colors.onSurfaceVariant
                                )
                                Text(
                                    clFormatter.format(total),
                                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                    color = colors.primary
                                )
                            }
                            Button(
                                onClick = { /* TODO: flujo de pago */ },
                                modifier = Modifier.height(56.dp)
                            ) {
                                Text("Pagar", style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(
    item: CartUiItem,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onRemove: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val fallbackRes = R.drawable.torta_chocolate
    val safeResId = if (item.product.imageResId != 0) item.product.imageResId else fallbackRes

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // IMAGEN
            Image(
                painter = painterResource(id = safeResId),
                contentDescription = item.product.name,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // CONTENIDO
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // NOMBRE Y ELIMINAR
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = item.product.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = colors.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            modifier = Modifier.size(20.dp),
                            tint = colors.error
                        )
                    }
                }

                // PRECIO
                Text(
                    text = clFormatter.format(item.product.price * item.quantity),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = colors.primary
                )

                // CONTROLES DE CANTIDAD
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilledTonalIconButton(
                        onClick = onDecrease,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = "Reducir",
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = item.quantity.toString(),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = colors.onSurface,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    FilledTonalIconButton(
                        onClick = onIncrease,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Aumentar",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
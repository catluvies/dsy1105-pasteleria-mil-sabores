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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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

/** Item de UI para el join carrito + productos */
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
                title = { Text("Carrito") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (uiItems.isNotEmpty()) {
                        TextButton(onClick = { viewModel.clearCart() }) { Text("Vaciar") }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.surface)
            )
        },
        containerColor = colors.surface
    ) { innerPadding ->
        if (uiItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tu carrito está vacío",
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.onSurfaceVariant
                )
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
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiItems, key = { it.product.id }) { item ->
                        CartRow(
                            item = item,
                            onDecrease = { viewModel.decrease(item.product.id) },
                            onIncrease = { viewModel.increase(item.product.id) },
                            onRemove = { viewModel.removeProduct(item.product.id) }
                        )
                    }
                }

                Surface(tonalElevation = 1.dp, color = colors.surfaceVariant) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total:", style = MaterialTheme.typography.titleMedium, color = colors.onSurface)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            clFormatter.format(total),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = colors.onSurface
                        )
                        Spacer(Modifier.weight(1f))
                        Button(onClick = { /* TODO: flujo de pago */ }, enabled = uiItems.isNotEmpty()) {
                            Text("Comprar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartRow(
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
        colors = CardDefaults.cardColors(containerColor = colors.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = safeResId),
                contentDescription = item.product.name,
                modifier = Modifier
                    .size(72.dp)
                    .padding(end = 12.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(item.product.name, style = MaterialTheme.typography.titleMedium, color = colors.onSurface)
                Spacer(Modifier.height(2.dp))
                Text(
                    "${clFormatter.format(item.product.price)} c/u",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )

                // Controles de cantidad
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalIconButton(onClick = onDecrease) {
                        Icon(Icons.Default.Remove, contentDescription = "Reducir")
                    }
                    Text(
                        text = item.quantity.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.onSurface
                    )
                    FilledTonalIconButton(onClick = onIncrease) {
                        Icon(Icons.Default.Add, contentDescription = "Aumentar")
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = clFormatter.format(item.product.price * item.quantity),
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.onSurface
                )
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}

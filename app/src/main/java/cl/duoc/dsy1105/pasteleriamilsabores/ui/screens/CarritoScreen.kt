package cl.duoc.dsy1105.pasteleriamilsabores.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.CartViewModel
import cl.duoc.dsy1105.pasteleriamilsabores.data.sampleProductList
import cl.duoc.dsy1105.pasteleriamilsabores.model.CartItem
import cl.duoc.dsy1105.pasteleriamilsabores.ui.theme.PasteleriaMilSaboresTheme
import cl.duoc.dsy1105.pasteleriamilsabores.data.CartDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.util.*

private val clFormatter = NumberFormat.getCurrencyInstance(
    Locale.Builder().setLanguage("es").setRegion("CL").build()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    onBack: () -> Unit,
    viewModel: CartViewModel
) {
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    // Unimos ids del carrito con los productos de muestra
    val lines = remember(cartItems) {
        val productsById = sampleProductList.associateBy { it.id }
        cartItems.mapNotNull { ci ->
            productsById[ci.productId]?.let { p ->
                Triple(p.name, p.price, ci)
            }
        }
    }
    val total = remember(lines) { lines.sumOf { it.second * it.third.quantity } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Total", style = MaterialTheme.typography.labelMedium)
                        Text(clFormatter.format(total), style = MaterialTheme.typography.headlineSmall)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { viewModel.clearCart() },
                            enabled = lines.isNotEmpty()
                        ) { Text("Vaciar") }
                        Button(
                            onClick = { /* TODO: flujo de pago */ },
                            enabled = lines.isNotEmpty()
                        ) { Text("Pagar") }
                    }
                }
            }
        }
    ) { inner ->
        if (lines.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(inner)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text("Tu carrito está vacío") }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(inner)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lines, key = { it.third.productId }) { (name, price, ci) ->
                    CartRow(
                        name = name,
                        price = price,
                        qty = ci.quantity,
                        onInc = { viewModel.addProduct(ci.productId, ci.quantity + 1) },
                        onDec = {
                            val q = ci.quantity - 1
                            if (q <= 0) viewModel.removeProduct(ci.productId)
                            else viewModel.addProduct(ci.productId, q)
                        },
                        onRemove = { viewModel.removeProduct(ci.productId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CartRow(
    name: String,
    price: Int,
    qty: Int,
    onInc: () -> Unit,
    onDec: () -> Unit,
    onRemove: () -> Unit
) {
    Card {
        Column(Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(1f)) {
                    Text(name, style = MaterialTheme.typography.titleMedium)
                    Text("${clFormatter.format(price)} c/u", style = MaterialTheme.typography.bodyMedium)
                }
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(onClick = onDec) { Text("−") }
                Text("$qty", style = MaterialTheme.typography.titleMedium)
                OutlinedButton(onClick = onInc) { Text("+") }
                Spacer(Modifier.weight(1f))
                val subtotal = price * qty
                Text("Subtotal: ${clFormatter.format(subtotal)}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

/* ===================== PREVIEWS ===================== */

/**
 * DAO en memoria solo para previews.
 * No toca Room real; evita errores de KAPT en el render.
 */
private class PreviewCartDao(initial: List<CartItem> = emptyList()) : CartDao {
    private val state = MutableStateFlow(initial)

    override fun getAll(): Flow<List<CartItem>> = state

    override suspend fun upsert(item: CartItem) {
        state.update { current ->
            val idx = current.indexOfFirst { it.productId == item.productId }
            if (idx == -1) current + item else current.toMutableList().also { it[idx] = item }
        }
    }

    override suspend fun delete(id: Int) {
        state.update { current -> current.filterNot { it.productId == id } }
    }

    override suspend fun clear() {
        state.value = emptyList()
    }
}

@Preview(showBackground = true, name = "Carrito vacío")
@Composable
private fun CarritoScreenPreviewEmpty() {
    val vm = remember { CartViewModel(PreviewCartDao(emptyList())) }
    PasteleriaMilSaboresTheme {
        CarritoScreen(onBack = {}, viewModel = vm)
    }
}

@Preview(showBackground = true, name = "Carrito con items")
@Composable
private fun CarritoScreenPreviewFilled() {
    val p0 = sampleProductList.getOrNull(0)
    val p1 = sampleProductList.getOrNull(1)
    val initial = listOfNotNull(
        p0?.let { CartItem(productId = it.id, quantity = 1) },
        p1?.let { CartItem(productId = it.id, quantity = 3) }
    )
    val vm = remember { CartViewModel(PreviewCartDao(initial)) }
    PasteleriaMilSaboresTheme {
        CarritoScreen(onBack = {}, viewModel = vm)
    }
}

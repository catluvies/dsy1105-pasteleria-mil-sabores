package cl.duoc.dsy1105.pasteleriamilsabores.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.dsy1105.pasteleriamilsabores.data.sampleProductList
import cl.duoc.dsy1105.pasteleriamilsabores.ui.components.ProductCard
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.CartViewModel

// Para preview-only
import cl.duoc.dsy1105.pasteleriamilsabores.ui.theme.PasteleriaMilSaboresTheme
import cl.duoc.dsy1105.pasteleriamilsabores.data.CartDao
import cl.duoc.dsy1105.pasteleriamilsabores.model.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onProfileClick: () -> Unit,
    onCartClick: () -> Unit,
    cartViewModel: CartViewModel,
    onProductClick: (Int) -> Unit   // ← nuevo
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuestros Productos", style = MaterialTheme.typography.headlineLarge) },
                actions = {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary) {
                        IconButton(onClick = onProfileClick) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Perfil",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary) {
                        IconButton(onClick = onCartClick) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "Carrito",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sampleProductList) { product ->
                // Contenedor clickable que envía el id al navegar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onProductClick(product.id) }
                ) {
                    ProductCard(
                        product = product,
                        onAddToCart = { p -> cartViewModel.addProduct(p.id, 1) }
                    )
                }
            }
        }
    }
}

/* ====== Preview-only helpers para que compile el preview sin Room real ====== */
class PreviewCartDao(initial: List<CartItem> = emptyList()) : CartDao {
    private val state = MutableStateFlow(initial)
    override fun getAll(): Flow<List<CartItem>> = state
    override suspend fun upsert(item: CartItem) {
        val list = state.value.toMutableList()
        val i = list.indexOfFirst { it.productId == item.productId }
        if (i >= 0) list[i] = item else list += item
        state.value = list
    }
    override suspend fun delete(id: Int) { state.value = state.value.filterNot { it.productId == id } }
    override suspend fun clear() { state.value = emptyList() }
}

@Preview(showBackground = true)
@Composable
private fun CatalogScreenPreview() {
    val vm = remember { CartViewModel(PreviewCartDao()) }
    PasteleriaMilSaboresTheme {
        CatalogScreen(
            onProfileClick = {},
            onCartClick = {},
            cartViewModel = vm,
            onProductClick = {}
        )
    }
}

package cl.duoc.dsy1105.pasteleriamilsabores.ui.screens

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.dsy1105.pasteleriamilsabores.data.sampleProductList
import cl.duoc.dsy1105.pasteleriamilsabores.ui.components.ProductCard
import cl.duoc.dsy1105.pasteleriamilsabores.ui.theme.PasteleriaMilSaboresTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(onProfileClick: () -> Unit) { // La firma ya está correcta
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuestros Productos", style = MaterialTheme.typography.headlineLarge) },
                actions = {
                    // ================== SOLUCIÓN ==================
                    // Volvemos a envolver el IconButton con el Surface
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        IconButton(onClick = onProfileClick) { // El onClick ya está conectado
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Perfil",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    // ===============================================

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        IconButton(onClick = { /* Lógica del carrito irá aquí */ }) {
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
                ProductCard(product = product)
            }
        }
    }
}

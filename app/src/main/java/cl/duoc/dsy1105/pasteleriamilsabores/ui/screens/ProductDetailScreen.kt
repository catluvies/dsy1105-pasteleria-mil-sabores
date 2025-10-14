package cl.duoc.dsy1105.pasteleriamilsabores.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.dsy1105.pasteleriamilsabores.R
import cl.duoc.dsy1105.pasteleriamilsabores.data.AppDatabase
import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import cl.duoc.dsy1105.pasteleriamilsabores.ui.theme.PasteleriaMilSaboresTheme
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    product: Product
) {
    // Create DB + ViewModel manually (no factory for simplicity)
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val cartViewModel = remember { CartViewModel(db.cartDao()) }

    var quantity by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = product.name, color = MaterialTheme.colorScheme.primary) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 🖼️ Product image
            Image(
                painter = painterResource(id = product.imageResId),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                contentScale = ContentScale.Crop
            )

            // 🧁 Product details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Product name
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Description header
                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )

                // Description text
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Price
                Text(
                    text = "Precio: $${product.price}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ➕ Quantity controls
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = { if (quantity > 1) quantity-- },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Text("-", style = MaterialTheme.typography.titleLarge)
                    }

                    Text(
                        text = quantity.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )

                    Button(
                        onClick = { quantity++ },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Text("+", style = MaterialTheme.typography.titleLarge)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 🛒 Add to cart button
                Button(
                    onClick = {
                        cartViewModel.addProduct(product.id, quantity)
                        println("🛒 Added ${product.name} x$quantity to cart")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar $quantity al carrito")
                }
            }
        }
    }
}

// 👇 Preview with sample product
@Preview(showBackground = true)
@Composable
fun ProductDetailsPreview() {
    val sample = Product(
        id = 1,
        name = "Torta de Chocolate",
        description = "Deliciosa torta húmeda con cobertura de ganache.",
        price = 15990,
        imageResId = R.drawable.torta_chocolate
    )

    PasteleriaMilSaboresTheme {
        ProductDetailsScreen(sample)
    }
}

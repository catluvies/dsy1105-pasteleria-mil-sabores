package cl.duoc.dsy1105.pasteleriamilsabores.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductManagementScreen(
    productViewModel: ProductViewModel,
    onNavigateBack: () -> Unit,
    onAddProduct: () -> Unit,
    onEditProduct: (Product) -> Unit
) {
    val products by productViewModel.products.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Productos") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFF8E1)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddProduct,
                containerColor = Color(0xFF8B4513)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Producto",
                    tint = Color.White
                )
            }
        },
        containerColor = Color(0xFFFFF8E1)
    ) { innerPadding ->
        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay productos",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF8D6E63)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products) { product ->
                    ProductManagementCard(
                        product = product,
                        onEdit = { onEditProduct(product) },
                        onDelete = { productViewModel.deleteProduct(product.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductManagementCard(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del producto
            Image(
                painter = painterResource(id = product.imageResId),
                contentDescription = product.name,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 12.dp)
            )

            // Información del producto
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF5D4037)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${product.price} CLP",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF8B4513)
                )
                if (product.description.isNotBlank()) {
                    Text(
                        text = product.description.take(50) + if (product.description.length > 50) "..." else "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8D6E63),
                        maxLines = 2
                    )
                }
            }

            // Botones de acción
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onEdit,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(0xFFFFCDD2)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Color(0xFFC62828)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(0xFFFFCDD2)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color(0xFFC62828)
                    )
                }
            }
        }
    }
}
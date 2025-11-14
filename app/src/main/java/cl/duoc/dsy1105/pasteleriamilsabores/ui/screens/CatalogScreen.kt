package cl.duoc.dsy1105.pasteleriamilsabores.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.duoc.dsy1105.pasteleriamilsabores.data.sampleProductList
import cl.duoc.dsy1105.pasteleriamilsabores.ui.components.ProductCard
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.CartViewModel
import cl.duoc.dsy1105.pasteleriamilsabores.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onProfileClick: () -> Unit,
    onCartClick: () -> Unit,
    cartViewModel: CartViewModel,
    onProductClick: (Int) -> Unit,
    productViewModel: ProductViewModel,
    darkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        productViewModel.seedIfEmpty(sampleProductList)
    }

    val products = productViewModel.products.collectAsStateWithLifecycle().value
    val cartItems = cartViewModel.cartItems.collectAsStateWithLifecycle().value
    val cartCount = cartItems.sumOf { it.quantity }

    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val showScrollButton by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 2
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Pastelería Mil Sabores",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            fontSize = 20.sp
                        )
                        Text(
                            "Nuestros Productos",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.secondaryContainer) {
                        IconButton(onClick = { onThemeChange(!darkTheme) }) {
                            Icon(
                                if (darkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = if (darkTheme) "Modo Claro" else "Modo Oscuro",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))

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

                    Box {
                        Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary) {
                            IconButton(onClick = onCartClick) {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    contentDescription = "Carrito",
                                    tint = MaterialTheme.colorScheme.onPrimary
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
                    Spacer(modifier = Modifier.width(8.dp))
                }
            )
        },
        floatingActionButton = {
            if (showScrollButton) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            gridState.animateScrollToItem(0)
                        }
                    }
                ) {
                    Icon(Icons.Default.ArrowUpward, contentDescription = "Ir arriba")
                }
            }
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = gridState,
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products, key = { it.id }) { product ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onProductClick(product.id) }
                ) {
                    ProductCard(product = product)
                }
            }
        }
    }
}
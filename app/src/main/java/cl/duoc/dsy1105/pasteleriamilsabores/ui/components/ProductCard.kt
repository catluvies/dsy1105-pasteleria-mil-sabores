package cl.duoc.dsy1105.pasteleriamilsabores.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cl.duoc.dsy1105.pasteleriamilsabores.R
import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import java.text.NumberFormat
import java.util.Locale

private val priceFormatter = NumberFormat.getCurrencyInstance(
    Locale.Builder().setLanguage("es").setRegion("CL").build()
)

@Composable
fun ProductCard(
    product: Product,
    onAddToCart: (Product) -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ✅ Evita crash: si imageResId == 0, usa un placeholder existente
            val fallback = R.drawable.torta_chocolate
            val painter = painterResource(id = if (product.imageResId != 0) product.imageResId else fallback)

            Image(
                painter = painter,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = priceFormatter.format(product.price),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

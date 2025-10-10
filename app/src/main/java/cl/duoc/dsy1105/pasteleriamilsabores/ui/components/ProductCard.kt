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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duoc.dsy1105.pasteleriamilsabores.data.sampleProductList
import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import cl.duoc.dsy1105.pasteleriamilsabores.ui.theme.PasteleriaMilSaboresTheme
import java.text.NumberFormat
import java.util.Locale

private val priceFormatter = NumberFormat.getCurrencyInstance(
    Locale.Builder().setLanguage("es").setRegion("CL").build()
)

@Composable
fun ProductCard(
    product: Product
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = product.imageResId),
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
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    PasteleriaMilSaboresTheme {
        val sampleProduct = sampleProductList.first()
        ProductCard(product = sampleProduct)
    }
}
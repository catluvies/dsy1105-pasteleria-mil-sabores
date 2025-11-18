package cl.duoc.dsy1105.pasteleriamilsabores

import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

/**
 * Prueba 1: Validación básica del modelo Product
 */
class ProductModelTest : DescribeSpec({
    describe("Product model") {
        it("debe crear un producto con todos sus campos") {
            val product = Product(
                id = 1,
                name = "Torta de Chocolate",
                description = "Deliciosa torta",
                price = 15000,
                imageUrl = "https://ejemplo.com/torta.jpg"
            )

            product.id shouldBe 1
            product.name shouldBe "Torta de Chocolate"
            product.price shouldBe 15000
        }
    }
})


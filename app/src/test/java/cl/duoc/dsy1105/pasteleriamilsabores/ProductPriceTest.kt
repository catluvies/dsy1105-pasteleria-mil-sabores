package cl.duoc.dsy1105.pasteleriamilsabores

import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

/**
 * Prueba 3: Cálculo de precio total
 */
class ProductPriceTest : DescribeSpec({
    describe("Product price calculation") {
        it("debe calcular correctamente el total con cantidad") {
            val product = Product(
                id = 1,
                name = "Pastel",
                description = "Rico pastel",
                price = 10000,
                imageUrl = "url"
            )
            val quantity = 3

            val total = product.price * quantity

            total shouldBe 30000
        }
    }
})


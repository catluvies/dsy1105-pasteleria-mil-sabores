package cl.duoc.dsy1105.pasteleriamilsabores

import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

/**
 * Prueba 6: Búsqueda de productos en lista
 */
class ProductListTest : DescribeSpec({
    describe("Product list operations") {
        it("debe encontrar un producto por ID") {
            val products = listOf(
                Product(1, "Torta", "Desc 1", 10000, "url1"),
                Product(2, "Brownie", "Desc 2", 5000, "url2")
            )

            val found = products.find { it.id == 2 }

            found?.name shouldBe "Brownie"
            found?.price shouldBe 5000
        }
    }
})
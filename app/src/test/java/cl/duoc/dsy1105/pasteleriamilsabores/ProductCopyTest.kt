package cl.duoc.dsy1105.pasteleriamilsabores

import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

/**
 * Prueba 5: Actualización de producto con copy
 */
class ProductCopyTest : DescribeSpec({
    describe("Product copy") {
        it("debe permitir copiar y modificar un producto") {
            val original = Product(
                id = 1,
                name = "Torta",
                description = "Desc",
                price = 10000,
                imageUrl = "url"
            )

            val actualizado = original.copy(price = 15000)

            actualizado.name shouldBe "Torta"
            actualizado.price shouldBe 15000
        }
    }
})


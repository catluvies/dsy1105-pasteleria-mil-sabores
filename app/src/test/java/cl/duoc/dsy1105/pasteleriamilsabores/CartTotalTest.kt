package cl.duoc.dsy1105.pasteleriamilsabores

import cl.duoc.dsy1105.pasteleriamilsabores.model.CartItem
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

/**
 * Prueba 4: Suma total de items en carrito
 */
class CartTotalTest : DescribeSpec({
    describe("Cart total calculation") {
        it("debe sumar correctamente las cantidades del carrito") {
            val items = listOf(
                CartItem(productId = 1, quantity = 2),
                CartItem(productId = 2, quantity = 3),
                CartItem(productId = 3, quantity = 1)
            )

            val total = items.sumOf { it.quantity }

            total shouldBe 6
        }
    }
})
package cl.duoc.dsy1105.pasteleriamilsabores

import cl.duoc.dsy1105.pasteleriamilsabores.model.CartItem
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

/**
 * Prueba 2: Validación básica del modelo CartItem
 */
class CartItemModelTest : DescribeSpec({
    describe("CartItem model") {
        it("debe crear un item con productId y quantity") {
            val item = CartItem(productId = 5, quantity = 3)

            item.productId shouldBe 5
            item.quantity shouldBe 3
        }
    }
})
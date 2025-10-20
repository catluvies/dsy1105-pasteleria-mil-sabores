package cl.duoc.dsy1105.pasteleriamilsabores

import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import cl.duoc.dsy1105.pasteleriamilsabores.model.ShoppingCart
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class CartTest {

    private lateinit var cart: ShoppingCart
    private lateinit var products: List<Product>

    @Before
    fun setup() {
        // Initialize a fresh cart and product list before each test
        cart = ShoppingCart()
        products = listOf(
            Product(1, "Torta de Chocolate", "Bizcocho húmedo con ganache", 15990, 0),
            Product(2, "Pie de Limón", "Suave y fresco", 12990, 0),
            Product(3, "Cupcake de Vainilla", "Decorado con crema pastelera", 2990, 0)
        )
    }

    // ✅ Add products
    @Test
    fun testAddProduct() {
        cart.addProduct(1, 2) // 2 tortas
        cart.addProduct(2, 1) // 1 pie

        val items = cart.getItems()

        assertEquals(2, items.size)
        assertEquals(2, items[1])
        assertEquals(1, items[2])
        assertEquals(3, cart.getTotalQuantity())
    }

    // ✅ Remove a quantity (but not the entire product)
    @Test
    fun testRemoveProductQuantity() {
        cart.addProduct(1, 3)
        cart.removeProduct(1, 1) // remove one

        val items = cart.getItems()
        assertEquals(2, items[1])
        assertEquals(2, cart.getTotalQuantity())
    }

    // ✅ Remove full product when quantity goes to zero
    @Test
    fun testRemoveFullProduct() {
        cart.addProduct(1, 2)
        cart.removeProduct(1, 2) // should delete product

        assertFalse(cart.getItems().containsKey(1))
        assertEquals(0, cart.getTotalQuantity())
    }

    // ✅ Clear one product manually
    @Test
    fun testClearSpecificProduct() {
        cart.addProduct(1, 2)
        cart.addProduct(2, 1)
        cart.removeProduct(1,2)

        val items = cart.getItems()
        assertFalse(items.containsKey(1))
        assertTrue(items.containsKey(2))
        assertEquals(1, items.size)
    }

    // ✅ Clear entire cart
    @Test
    fun testClearCart() {
        cart.addProduct(1, 2)
        cart.addProduct(2, 1)
        cart.clearCart()

        assertTrue(cart.getItems().isEmpty())
        assertEquals(0, cart.getTotalQuantity())
    }

    // ✅ Calculate total correctly
    @Test
    fun testGetTotalPrice() {
        cart.addProduct(1, 2) // 2 * 15990
        cart.addProduct(2, 1) // 1 * 12990
        val total = cart.getTotal(products)

        assertEquals(44970, total)
    }

    // ✅ Read cart content (visual test)
    @Test
    fun testReadCartContent() {
        cart.addProduct(1, 1)
        cart.addProduct(2, 1)
        cart.addProduct(3, 1)

        val items = cart.getItems()
        println("🛒 Cart Contents:")
        items.forEach { (id, qty) ->
            val product = products.find { it.id == id }
            println("- ${product?.name}: $qty unidades")
        }

        assertEquals(3, items.size)
        assertEquals(3, cart.getTotalQuantity())
    }
}

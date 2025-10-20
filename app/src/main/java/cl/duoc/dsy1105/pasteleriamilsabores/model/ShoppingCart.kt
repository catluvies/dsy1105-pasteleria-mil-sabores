package cl.duoc.dsy1105.pasteleriamilsabores.model

class ShoppingCart {
    private val items = mutableMapOf<Int, Int>() // productId → qty

    fun addProduct(id: Int, qty: Int = 1) { items[id] = (items[id] ?: 0) + qty }
    fun removeProduct(id: Int, qty: Int = 1) {
        val current = items[id] ?: return
        if (current <= qty) items.remove(id) else items[id] = current - qty
    }
    fun clearCart() { items.clear() }
    fun getItems(): Map<Int, Int> = items.toMap()
    fun getTotalQuantity(): Int = items.values.sum()
    fun getTotal(products: List<Product>): Int =
        items.entries.sumOf { entry ->
            val product = products.find { it.id == entry.key }
            (product?.price ?: 0) * entry.value
        }
}

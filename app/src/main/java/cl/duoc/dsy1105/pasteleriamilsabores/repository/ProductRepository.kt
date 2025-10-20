package cl.duoc.dsy1105.pasteleriamilsabores.repository

import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductRepository {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    fun setProducts(productList: List<Product>) {
        _products.value = productList
    }

    fun addProduct(product: Product) {
        val currentList = _products.value.toMutableList()
        currentList.add(product)
        _products.value = currentList
    }

    fun updateProduct(product: Product) {
        val currentList = _products.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == product.id }
        if (index != -1) {
            currentList[index] = product
            _products.value = currentList
        }
    }

    fun deleteProduct(productId: Int) {
        _products.value = _products.value.filter { it.id != productId }
    }

    fun getProductById(id: Int): Product? {
        return _products.value.find { it.id == id }
    }

    fun getNextProductId(): Int {
        return (_products.value.maxOfOrNull { it.id } ?: 0) + 1
    }
}
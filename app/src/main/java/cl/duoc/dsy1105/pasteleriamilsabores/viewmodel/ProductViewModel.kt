package cl.duoc.dsy1105.pasteleriamilsabores.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import cl.duoc.dsy1105.pasteleriamilsabores.repository.ProductRepository
import kotlinx.coroutines.flow.StateFlow

class ProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    val products: StateFlow<List<Product>> = productRepository.products

    fun addProduct(product: Product) {
        productRepository.addProduct(product)
    }

    fun updateProduct(product: Product) {
        productRepository.updateProduct(product)
    }

    fun deleteProduct(productId: Int) {
        productRepository.deleteProduct(productId)
    }

    fun getNextProductId(): Int {
        return productRepository.getNextProductId()
    }

    fun getProductById(id: Int): Product? {
        return productRepository.getProductById(id)
    }
}
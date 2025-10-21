package cl.duoc.dsy1105.pasteleriamilsabores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import cl.duoc.dsy1105.pasteleriamilsabores.repository.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProductViewModel(
    private val repo: ProductRepository
) : ViewModel() {

    val products: StateFlow<List<Product>> =
        repo.products.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addProduct(product: Product) {
        viewModelScope.launch { repo.addProduct(product) }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch { repo.updateProduct(product) }
    }

    fun deleteProduct(productId: Int) {
        viewModelScope.launch { repo.deleteProduct(productId) }
    }

    /** Mantiene tu API síncrona actual */
    fun getNextProductId(): Int = runBlocking { repo.getNextProductId() }

    /** Consulta puntual (si la necesitas) */
    fun getProductById(id: Int): Product? = runBlocking { repo.getProductById(id) }

    /** Sembrado inicial */
    fun seedIfEmpty(sample: List<Product>) {
        viewModelScope.launch { repo.seedIfEmpty(sample) }
    }
}

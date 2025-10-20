package cl.duoc.dsy1105.pasteleriamilsabores.repository

import cl.duoc.dsy1105.pasteleriamilsabores.data.ProductDao
import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(
    private val dao: ProductDao
) {
    val products: Flow<List<Product>> = dao.getAll()

    suspend fun addProduct(product: Product) = dao.upsert(product)

    suspend fun updateProduct(product: Product) = dao.upsert(product)

    suspend fun deleteProduct(productId: Int) = dao.delete(productId)

    suspend fun getNextProductId(): Int = dao.nextId()

    suspend fun getProductById(id: Int): Product? = dao.getById(id)

    /** Siembra inicial si la tabla está vacía */
    suspend fun seedIfEmpty(sample: List<Product>) {
        if (dao.count() == 0) {
            dao.insertAll(sample)
        }
    }
}

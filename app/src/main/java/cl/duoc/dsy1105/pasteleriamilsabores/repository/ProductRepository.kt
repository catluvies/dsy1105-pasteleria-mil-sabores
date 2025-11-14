package cl.duoc.dsy1105.pasteleriamilsabores.repository

import cl.duoc.dsy1105.pasteleriamilsabores.data.CartDao
import cl.duoc.dsy1105.pasteleriamilsabores.data.ProductDao
import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(
    private val productDao: ProductDao,
    private val cartDao: CartDao
) {
    val products: Flow<List<Product>> = productDao.getAll()

    suspend fun addProduct(product: Product) = productDao.upsert(product)

    suspend fun updateProduct(product: Product) = productDao.upsert(product)

    suspend fun deleteProduct(productId: Int) {
        // PRIMERO: Eliminar del carrito (si existe)
        cartDao.delete(productId)
        // DESPUÉS: Eliminar el producto
        productDao.delete(productId)
    }

    suspend fun getNextProductId(): Int = productDao.nextId()

    suspend fun getProductById(id: Int): Product? = productDao.getById(id)

    /** Siembra inicial si la tabla está vacía */
    suspend fun seedIfEmpty(sample: List<Product>) {
        if (productDao.count() == 0) {
            productDao.insertAll(sample)
        }
    }
}
package cl.duoc.dsy1105.pasteleriamilsabores.repository

import android.util.Log
import cl.duoc.dsy1105.pasteleriamilsabores.api.RetrofitClient
import cl.duoc.dsy1105.pasteleriamilsabores.data.CartDao
import cl.duoc.dsy1105.pasteleriamilsabores.data.ProductDao
import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(
    private val productDao: ProductDao,
    private val cartDao: CartDao
) {
    private val apiService = RetrofitClient.productApiService
    private val TAG = "ProductRepository"

    val products: Flow<List<Product>> = productDao.getAll()

    /**
     * Sincroniza productos desde el backend y actualiza Room
     */
    suspend fun syncProductsFromBackend(): Boolean {
        return try {
            Log.d(TAG, "Sincronizando productos desde backend...")
            val response = apiService.getAllProducts()

            if (response.isSuccessful) {
                val productsFromBackend = response.body() ?: emptyList()
                Log.d(TAG, "Productos recibidos: ${productsFromBackend.size}")

                // Limpiar y actualizar Room con datos del backend
                productDao.deleteAll()
                if (productsFromBackend.isNotEmpty()) {
                    productDao.insertAll(productsFromBackend)
                }

                Log.d(TAG, "Sincronización exitosa")
                true
            } else {
                Log.e(TAG, "Error respuesta backend: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sincronización: ${e.message}", e)
            false
        }
    }

    suspend fun addProduct(product: Product) {
        try {
            // Intentar guardar en backend
            Log.d(TAG, "Enviando producto al backend: ${product.name}")
            val response = apiService.createProduct(product)
            if (response.isSuccessful) {
                val savedProduct = response.body()!!
                Log.d(TAG, "Producto guardado en backend ID: ${savedProduct.id}")
                // Guardar en Room
                productDao.upsert(savedProduct)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(TAG, "Error backend: ${response.code()}")
                Log.e(TAG, "Error body: $errorBody")
                // Guardar solo en Room si falla
                productDao.upsert(product)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Sin conexión, guardando localmente: ${e.message}", e)
            // Sin internet, guardar solo en Room
            productDao.upsert(product)
        }
    }

    suspend fun updateProduct(product: Product) {
        try {
            // Intentar actualizar en backend
            val response = apiService.updateProduct(product.id, product)
            if (response.isSuccessful) {
                val updatedProduct = response.body()!!
                Log.d(TAG, "Producto actualizado en backend")
                // Actualizar en Room
                productDao.upsert(updatedProduct)
            } else {
                Log.e(TAG, "Error backend: ${response.code()}")
                // Actualizar solo en Room si falla
                productDao.upsert(product)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Sin conexión, actualizando localmente: ${e.message}")
            // Sin internet, actualizar solo en Room
            productDao.upsert(product)
        }
    }

    suspend fun deleteProduct(productId: Int) {
        try {
            // Intentar eliminar del backend
            val response = apiService.deleteProduct(productId)
            if (response.isSuccessful) {
                Log.d(TAG, "Producto eliminado del backend")
            } else {
                Log.e(TAG, "Error backend: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Sin conexión, eliminando solo localmente: ${e.message}")
        }

        // Siempre eliminar de Room y carrito
        cartDao.delete(productId)
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
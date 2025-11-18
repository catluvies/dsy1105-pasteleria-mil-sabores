package cl.duoc.dsy1105.pasteleriamilsabores.data

import androidx.room.*
import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM products ORDER BY id ASC")
    fun getAll(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(product: Product)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(products: List<Product>)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM products")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM products")
    suspend fun count(): Int

    @Query("SELECT COALESCE(MAX(id), 0) + 1 FROM products")
    suspend fun nextId(): Int

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Product?
}

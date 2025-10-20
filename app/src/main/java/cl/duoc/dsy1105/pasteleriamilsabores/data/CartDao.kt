package cl.duoc.dsy1105.pasteleriamilsabores.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cl.duoc.dsy1105.pasteleriamilsabores.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getAll(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: CartItem)

    @Query("DELETE FROM cart_items WHERE productId = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clear()
}

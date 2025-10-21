package cl.duoc.dsy1105.pasteleriamilsabores.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    @DrawableRes val imageResId: Int
)

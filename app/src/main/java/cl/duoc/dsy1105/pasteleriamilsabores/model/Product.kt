package cl.duoc.dsy1105.pasteleriamilsabores.model

import androidx.annotation.DrawableRes

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    @DrawableRes val imageResId: Int
)
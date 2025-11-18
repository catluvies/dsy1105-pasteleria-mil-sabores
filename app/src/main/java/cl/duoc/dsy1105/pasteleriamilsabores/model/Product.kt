package cl.duoc.dsy1105.pasteleriamilsabores.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = false)
    @Expose(serialize = false, deserialize = true) // NO enviar al crear, SÍ recibir del backend
    val id: Int = 0,

    @Expose
    val name: String,

    @Expose
    val description: String,

    @Expose
    val price: Int,

    @Expose
    val imageUrl: String
)

package cl.duoc.dsy1105.pasteleriamilsabores.api

import cl.duoc.dsy1105.pasteleriamilsabores.model.Product
import retrofit2.Response
import retrofit2.http.*

/**
 * API service para comunicarse con el backend Spring Boot
 */
interface ProductApiService {

    @GET("products")
    suspend fun getAllProducts(): Response<List<Product>>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<Product>

    @POST("products")
    suspend fun createProduct(@Body product: Product): Response<Product>

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: Product): Response<Product>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Void>
}

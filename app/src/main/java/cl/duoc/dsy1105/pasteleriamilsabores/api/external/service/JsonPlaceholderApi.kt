package cl.duoc.dsy1105.pasteleriamilsabores.api.external.service

import cl.duoc.dsy1105.pasteleriamilsabores.api.external.models.Post
import retrofit2.Response
import retrofit2.http.GET

interface JsonPlaceholderApi {

    @GET("posts")
    suspend fun getAllPosts(): Response<List<Post>>
}
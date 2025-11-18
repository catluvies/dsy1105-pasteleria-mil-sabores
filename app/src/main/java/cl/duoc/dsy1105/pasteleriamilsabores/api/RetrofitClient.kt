package cl.duoc.dsy1105.pasteleriamilsabores.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit para comunicarse con el backend Spring Boot
 */
object RetrofitClient {

    // URL base del backend (10.0.2.2 es localhost desde el emulador)
    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    // Logging interceptor para debugging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente HTTP con timeouts configurados
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Gson configurado para NO serializar id = 0
    private val gson = GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation() // Usaremos @Expose para controlar qué se envía
        .create()

    // Instancia de Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    // Servicio de API de productos
    val productApiService: ProductApiService = retrofit.create(ProductApiService::class.java)
}

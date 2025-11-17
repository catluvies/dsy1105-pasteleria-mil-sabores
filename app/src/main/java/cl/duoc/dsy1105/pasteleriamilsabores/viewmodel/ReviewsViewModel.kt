package cl.duoc.dsy1105.pasteleriamilsabores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.dsy1105.pasteleriamilsabores.api.external.client.RetrofitClient
import cl.duoc.dsy1105.pasteleriamilsabores.api.external.models.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ReviewsViewModel : ViewModel() {

    private val _reviews = MutableStateFlow<List<Post>>(emptyList())
    val reviews: StateFlow<List<Post>> = _reviews.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    fun loadReviews() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = RetrofitClient.jsonPlaceholderApi.getAllPosts()

                if (response.isSuccessful) {
                    val posts = response.body() ?: emptyList()
                    // Tomamos solo los primeros 15 para simular reseñas de clientes
                    _reviews.value = posts.take(15)
                } else {
                    _errorMessage.value = "Error al cargar reseñas: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun clearError() {
        _errorMessage.value = null
    }
}
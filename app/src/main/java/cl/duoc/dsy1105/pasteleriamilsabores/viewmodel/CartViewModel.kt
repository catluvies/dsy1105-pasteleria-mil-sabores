package cl.duoc.dsy1105.pasteleriamilsabores.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.dsy1105.pasteleriamilsabores.data.CartDao
import cl.duoc.dsy1105.pasteleriamilsabores.model.CartItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(private val dao: CartDao) : ViewModel() {

    val cartItems: StateFlow<List<CartItem>> =
        dao.getAll().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addProduct(productId: Int, quantity: Int) {
        viewModelScope.launch { dao.upsert(CartItem(productId, quantity)) }
    }

    fun removeProduct(productId: Int) {
        viewModelScope.launch { dao.delete(productId) }
    }

    fun clearCart() {
        viewModelScope.launch { dao.clear() }
    }
}

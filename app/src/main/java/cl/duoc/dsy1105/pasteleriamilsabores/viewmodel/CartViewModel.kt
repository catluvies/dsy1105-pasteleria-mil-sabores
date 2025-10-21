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

    /** Suma la cantidad indicada al valor actual (útil desde ProductDetails). */
    fun addProduct(productId: Int, quantity: Int) {
        viewModelScope.launch {
            val current = cartItems.value.firstOrNull { it.productId == productId }?.quantity ?: 0
            val newQty = (current + quantity).coerceIn(1, 99)
            dao.upsert(CartItem(productId, newQty))
        }
    }

    /** Fija la cantidad exacta (si ≤0 elimina). */
    fun setQuantity(productId: Int, newQuantity: Int) {
        viewModelScope.launch {
            if (newQuantity <= 0) {
                dao.delete(productId)
            } else {
                dao.upsert(CartItem(productId, newQuantity.coerceAtMost(99)))
            }
        }
    }

    fun increase(productId: Int) {
        val current = cartItems.value.firstOrNull { it.productId == productId }?.quantity ?: 0
        setQuantity(productId, current + 1)
    }

    fun decrease(productId: Int) {
        val current = cartItems.value.firstOrNull { it.productId == productId }?.quantity ?: 0
        setQuantity(productId, current - 1)
    }

    fun removeProduct(productId: Int) {
        viewModelScope.launch { dao.delete(productId) }
    }

    fun clearCart() {
        viewModelScope.launch { dao.clear() }
    }
}

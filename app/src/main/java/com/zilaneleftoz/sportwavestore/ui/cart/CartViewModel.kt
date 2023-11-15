package com.zilaneleftoz.sportwavestore.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zilaneleftoz.sportwavestore.common.Resource
import com.zilaneleftoz.sportwavestore.data.model.response.ProductUI
import com.zilaneleftoz.sportwavestore.data.repository.ProductRepository
import com.zilaneleftoz.sportwavestore.data.repository.UserAuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userRepository: UserAuthenticationRepository
) : ViewModel() {

    private var _cartState = MutableLiveData<CartState>()
    val cartState: LiveData<CartState> get() = _cartState

    fun getCartProducts() = viewModelScope.launch {
        _cartState.value = CartState.Loading

        _cartState.value = when (val result = productRepository.getCartProducts(userRepository.getUserId())) {
            is Resource.Success -> CartState.SuccessState(result.data)
            is Resource.Fail -> CartState.EmptyScreen(result.failMessage)
            is Resource.Error -> CartState.ShowPopUp(result.errorMessage)
        }
    }

    fun deleteFromCart(id: Int) = viewModelScope.launch {
        val result = productRepository.deleteFromCart(id, userRepository.getUserId())
        if (result is Resource.Success) {
            _cartState.value = CartState.DeleteSuccess(result.data)
        }
        getCartProducts()
    }

    fun clearCart() = viewModelScope.launch {
        val result = productRepository.clearCart(userRepository.getUserId())
        if (result is Resource.Success) {
            _cartState.value = CartState.DeleteSuccess(result.data)
        }
        getCartProducts()
    }
}

sealed interface CartState {
    object Loading : CartState
    data class SuccessState(val products: List<ProductUI>) : CartState
    data class EmptyScreen(val failMessage: String) : CartState
    data class ShowPopUp(val errorMessage: String) : CartState
    data class DeleteSuccess(val successMessage: String) : CartState
}
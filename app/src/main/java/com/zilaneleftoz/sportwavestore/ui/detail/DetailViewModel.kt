package com.zilaneleftoz.sportwavestore.ui.detail

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
class DetailViewModel  @Inject constructor(
    private val productRepository: ProductRepository,
    private val userRepository: UserAuthenticationRepository
) : ViewModel() {

    private var _detailState = MutableLiveData<DetailState>()
    val detailState: LiveData<DetailState> get() = _detailState

    fun getProductDetail(id: Int) = viewModelScope.launch {
        _detailState.value = DetailState.Loading

        _detailState.value = when (val result = productRepository.getProductDetail(userRepository.getUserId(), id)) {
            is Resource.Success -> DetailState.SuccessState(result.data)
            is Resource.Fail -> DetailState.EmptyScreen(result.failMessage)
            is Resource.Error -> DetailState.ShowPopUp(result.errorMessage)
        }
    }

    fun addToCart(productId: Int) = viewModelScope.launch {
        val result = productRepository.addToCart(userRepository.getUserId(), productId)
        if (result is Resource.Success) {
            _detailState.value = DetailState.CartAddSuccess(result.data)
        }
        if (result is Resource.Fail){
            _detailState.value = DetailState.CartAddFail(result.failMessage)
        }
    }

    fun setFavoriteState(product: ProductUI) = viewModelScope.launch {
        if (product.isFav) {
            productRepository.deleteFromFavorites(product, userRepository.getUserId())
        } else {
            productRepository.addToFavorites(product, userRepository.getUserId())
        }
        getProductDetail(product.id)
    }
}

sealed interface DetailState {
    object Loading : DetailState
    data class SuccessState(val product: ProductUI) : DetailState
    data class EmptyScreen(val failMessage: String) : DetailState
    data class ShowPopUp(val errorMessage: String) : DetailState
    data class CartAddSuccess(val successMessage: String) : DetailState
    data class CartAddFail(val failMessage: String) : DetailState
}
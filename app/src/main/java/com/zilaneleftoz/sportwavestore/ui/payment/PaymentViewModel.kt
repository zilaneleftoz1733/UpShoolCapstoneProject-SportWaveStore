package com.zilaneleftoz.sportwavestore.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zilaneleftoz.sportwavestore.data.repository.ProductRepository
import com.zilaneleftoz.sportwavestore.data.repository.UserAuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userRepository: UserAuthenticationRepository
):
    ViewModel() {

    private var _paymentState = MutableLiveData<PaymentState>()
    val paymentState: LiveData<PaymentState> get() = _paymentState

    fun clearCart() = viewModelScope.launch {
        productRepository.clearCart(userRepository.getUserId())
    }

    fun payment(number: String,
                cvc: String,
                name: String,
                selectedMonth: String?,
                selectedYear: String?) = viewModelScope.launch {

        _paymentState.value = PaymentState.Loading

        if (!checkFields(number, cvc, name, selectedMonth, selectedYear)) {
            return@launch
        }

        else {
            _paymentState.value = PaymentState.SuccessState(true)
        }
    }

    private fun checkFields(number: String,
                            cvc: String,
                            name: String,
                            selectedMonth: String?,
                            selectedYear: String?): Boolean {
        return when {
            number.length < 16 -> {
                _paymentState.value = PaymentState.ShowPopUp("Card number cannot be less than 16")
                false
            }
            cvc.length < 3 -> {
                _paymentState.value = PaymentState.ShowPopUp("CVC must consist of 3 digits")
                false
            }
            name.isEmpty() -> {
                _paymentState.value = PaymentState.ShowPopUp("Name can not be empty!")
                false
            }

            selectedMonth.isNullOrEmpty() -> {
                _paymentState.value = PaymentState.ShowPopUp("Please select a month")
                false
            }

            selectedYear.isNullOrEmpty() -> {
                _paymentState.value = PaymentState.ShowPopUp("Please select a year")
                false
            }

            else -> true
        }
    }
}

sealed interface PaymentState {
    object Loading : PaymentState
    data class SuccessState(val successState: Boolean) : PaymentState
    data class ShowPopUp(val errorMessage: String) : PaymentState
}
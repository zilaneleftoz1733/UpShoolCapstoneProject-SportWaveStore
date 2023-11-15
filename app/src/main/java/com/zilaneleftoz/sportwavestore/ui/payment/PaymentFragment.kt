package com.zilaneleftoz.sportwavestore.ui.payment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.zilaneleftoz.sportwavestore.R
import com.zilaneleftoz.sportwavestore.common.gone
import com.zilaneleftoz.sportwavestore.common.viewBinding
import com.zilaneleftoz.sportwavestore.common.visible
import com.zilaneleftoz.sportwavestore.databinding.FragmentPaymentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private val binding by viewBinding(FragmentPaymentBinding::bind)

    private val viewModel by viewModels<PaymentViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            val months = resources.getStringArray(R.array.months)
            val monthAdapter = ArrayAdapter(requireContext(), R.layout.item_menu, months)
            etMonth.setAdapter(monthAdapter)

            val years = resources.getStringArray(R.array.years)
            val yearAdapter = ArrayAdapter(requireContext(), R.layout.item_menu, years)
            etYear.setAdapter(yearAdapter)

            etMonth.setOnItemClickListener { _, _, position, _ ->
                months[position]
            }

            etYear.setOnItemClickListener { _, _, position, _ ->
                years[position]
            }

            buy.setOnClickListener {
                val number = etNumber.text.toString()
                val cvc = etCVC.text.toString()
                val name = etCardName.text.toString()
                val selectedMonth = etMonth.text.toString()
                val selectedYear = etYear.text.toString()

                viewModel.payment(number, cvc, name, selectedMonth, selectedYear)
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.paymentState.observe(viewLifecycleOwner) {state ->
            when(state) {
                PaymentState.Loading -> progressBarPayment.visible()

                is PaymentState.SuccessState -> {
                    progressBarPayment.gone()
                    findNavController().navigate(R.id.paymentToSuccess)
                    viewModel.clearCart()
                }

                is PaymentState.ShowPopUp -> {
                    progressBarPayment.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }
}
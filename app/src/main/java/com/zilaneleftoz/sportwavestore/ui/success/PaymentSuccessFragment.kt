package com.zilaneleftoz.sportwavestore.ui.success

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zilaneleftoz.sportwavestore.R
import com.zilaneleftoz.sportwavestore.common.viewBinding
import com.zilaneleftoz.sportwavestore.databinding.FragmentPaymentSuccessBinding

class SuccessFragment : Fragment(R.layout.fragment_payment_success) {

    private val binding by viewBinding(FragmentPaymentSuccessBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackHome.setOnClickListener {
            findNavController().navigate(R.id.successsToHome)
        }
    }
}

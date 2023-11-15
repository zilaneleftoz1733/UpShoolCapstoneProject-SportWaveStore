package com.zilaneleftoz.sportwavestore.ui.signUp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.zilaneleftoz.sportwavestore.R
import com.zilaneleftoz.sportwavestore.common.gone
import com.zilaneleftoz.sportwavestore.common.viewBinding
import com.zilaneleftoz.sportwavestore.common.visible
import com.zilaneleftoz.sportwavestore.data.model.User
import com.zilaneleftoz.sportwavestore.databinding.FragmentSignUpBinding
import com.zilaneleftoz.sportwavestore.ui.login.signUp.SignUpState
import com.zilaneleftoz.sportwavestore.ui.login.signUp.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val binding by viewBinding(FragmentSignUpBinding::bind)

    private val viewModel by viewModels<SignUpViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnSignUp.setOnClickListener {
                viewModel.signUp(
                    User(
                        name = etName.text.toString(),
                        surname = etSurname.text.toString(),
                        email = etEmail.text.toString()
                    ), etPassword.text.toString()
                )
            }
            btnToSignIn.setOnClickListener {
                findNavController().navigate(R.id.signUpToSignIn)
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.signUpState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SignUpState.Loading -> progressBarSignUp.visible()

                is SignUpState.GoToHome -> {
                    progressBarSignUp.gone()
                    findNavController().navigate(R.id.signUpToHome)
                }

                is SignUpState.ShowPopUp -> {
                    progressBarSignUp.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }
}
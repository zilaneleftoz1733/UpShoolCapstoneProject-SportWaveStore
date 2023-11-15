package com.zilaneleftoz.sportwavestore.ui.signIn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.zilaneleftoz.sportwavestore.R
import com.zilaneleftoz.sportwavestore.common.gone
import com.zilaneleftoz.sportwavestore.common.viewBinding
import com.zilaneleftoz.sportwavestore.common.visible
import com.zilaneleftoz.sportwavestore.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class SignInFragment : Fragment(R.layout.fragment_sign_in) {
        private val binding by viewBinding(FragmentSignInBinding::bind)

        private val viewModel by viewModels<SignInViewModel>()

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            with(binding) {
                btnSignIn.setOnClickListener {
                    val email = etEmail.text.toString()
                    val password = etPassword.text.toString()
                    viewModel.signIn(email, password)
                }
                btnToSignUp.setOnClickListener {
                    findNavController().navigate(R.id.signInToSignUp)
                }
            }

            observeData()
        }

        private fun observeData() = with(binding) {
            viewModel.signInState.observe(viewLifecycleOwner){ state ->
                when (state) {
                    SignInState.Loading -> progressBarSignIn.visible()

                    is SignInState.GoToHome -> {
                        progressBarSignIn.gone()
                        findNavController().navigate(R.id.signInToHome)
                    }

                    is SignInState.ShowPopUp -> {
                        progressBarSignIn.gone()
                        Snackbar.make(requireView(), state.errorMessage, 1000).show()
                    }
                }
            }
        }
    }
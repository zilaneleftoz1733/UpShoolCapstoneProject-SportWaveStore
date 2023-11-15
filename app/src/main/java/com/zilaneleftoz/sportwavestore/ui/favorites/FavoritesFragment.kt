package com.zilaneleftoz.sportwavestore.ui.favorites

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
import com.zilaneleftoz.sportwavestore.data.model.response.ProductUI
import com.zilaneleftoz.sportwavestore.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val binding by viewBinding(FragmentFavoritesBinding::bind)

    private val viewModel by viewModels<FavoritesViewModel>()

    private val favAdapter = FavoriteProductsAdapter(onDeleteClick = ::onDeleteFromFav, onProductClick = ::onProductClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavorites()

        with(binding) {
            rvFav.adapter = favAdapter
            clearFavs.setOnClickListener {
                viewModel.clearFavorites()
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.favoritesState.observe(viewLifecycleOwner) { state ->
            when (state) {
                FavoritesState.Loading -> progressBarFav.visible()

                is FavoritesState.SuccessState -> {
                    progressBarFav.gone()
                    favAdapter.submitList(state.products)
                }

                is FavoritesState.EmptyScreen -> {
                    progressBarFav.gone()
                    ivEmpty.visible()
                    tvEmpty.visible()
                    rvFav.gone()
                    tvEmpty.text = state.failMessage
                }

                is FavoritesState.ShowPopUp -> {
                    progressBarFav.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }

    private fun onDeleteFromFav(product: ProductUI) {
        viewModel.deleteFromFavorites(product)
    }

    private fun onProductClick(id: Int) {
        findNavController().navigate(R.id.favToDetail)
    }
}
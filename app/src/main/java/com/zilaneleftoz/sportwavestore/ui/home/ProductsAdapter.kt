package com.zilaneleftoz.sportwavestore.ui.home

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zilaneleftoz.sportwavestore.R
import com.zilaneleftoz.sportwavestore.common.gone
import com.zilaneleftoz.sportwavestore.common.visible
import com.zilaneleftoz.sportwavestore.data.model.response.ProductUI
import com.zilaneleftoz.sportwavestore.databinding.HomeCartBinding

class ProductsAdapter(
    private val onFavClick: (ProductUI) -> Unit,
    private val onProductClick: (Int) -> Unit
) : ListAdapter<ProductUI, ProductsAdapter.ProductsHolder>(ProductDiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsHolder {
        return ProductsHolder(
            HomeCartBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onFavClick,
            onProductClick
        )
    }

    override fun onBindViewHolder(holder: ProductsHolder, position: Int) =holder.bind(getItem(position))

    class ProductsHolder(
        private val binding: HomeCartBinding,
        private val onFavClick: (ProductUI) -> Unit,
        private val onProductClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) {
            with(binding) {
                productTitle.text = product.title
                productCat.text = product.category

                productFav.setBackgroundResource(
                    if(product.isFav) R.drawable.ic_selected
                    else R.drawable.ic_unselected
                )

                productPrice.text = "${product.price} £"
                if (product.saleState) {
                    productPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    productSale.text = "${product.salePrice} £"
                    productSale.visible()
                } else {
                    productPrice.paintFlags = 0
                    productSale.gone()
                }

                Glide.with(productImg1).load(product.imageOne).into(productImg1)
                productFavorites.setOnClickListener {
                    onFavClick(product)
                }

                root.setOnClickListener {
                    onProductClick(product.id)
                }
            }
        }
    }

    class ProductDiffUtilCallBack : DiffUtil.ItemCallback<ProductUI>() {
        override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
            return oldItem == newItem
        }
    }
}
package com.zilaneleftoz.sportwavestore.data.model.response


data class GetProductsResponse(
    val products: List<Product>?,
    val status: Int?,
    val message: String?
)


package com.zilaneleftoz.sportwavestore.data.repository

import com.zilaneleftoz.sportwavestore.common.Resource
import com.zilaneleftoz.sportwavestore.data.mapper.mapProductEntityToProductUI
import com.zilaneleftoz.sportwavestore.data.mapper.mapProductToProductUI
import com.zilaneleftoz.sportwavestore.data.mapper.mapToProductEntity
import com.zilaneleftoz.sportwavestore.data.mapper.mapToProductUI
import com.zilaneleftoz.sportwavestore.data.model.request.AddToCartRequest
import com.zilaneleftoz.sportwavestore.data.model.request.ClearCartRequest
import com.zilaneleftoz.sportwavestore.data.model.request.DeleteFromCartRequest
import com.zilaneleftoz.sportwavestore.data.model.response.ProductUI
import com.zilaneleftoz.sportwavestore.data.source.local.ProductDao
import com.zilaneleftoz.sportwavestore.data.source.remote.ProductService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class  ProductRepository(private val productService: ProductService,
private val productDao: ProductDao
) {

    suspend fun getProducts(userId: String): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val favorites = productDao.getProductIds(userId)
                val response = productService.getProducts().body()

                if (response?.status == 200) {
                    Resource.Success(response.products.orEmpty().mapProductToProductUI(favorites))
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun getSaleProducts(): Resource<List<ProductUI>>  =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.getSaleProducts().body()

                if (response?.status == 200) {
                    Resource.Success(response.products.orEmpty().mapProductToProductUI())
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun getSearchProducts(query: String): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.getSearchProduct(query).body()

                if (response?.status == 200) {
                    Resource.Success(response.products.orEmpty().mapProductToProductUI())
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun getProductDetail(userId: String, id: Int): Resource<ProductUI> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.getProductDetail(id).body()
                val favorites = productDao.getProductIds(userId)

                if (response?.status == 200 && response.product != null) {
                    Resource.Success(response.product.mapToProductUI(favorites))
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun getCartProducts(userId: String): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.getCartProducts(userId).body()

                if (response?.status == 200) {
                    Resource.Success(response.products.orEmpty().mapProductToProductUI())
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun addToCart(userId: String, productId: Int): Resource<String> =
        withContext(Dispatchers.IO) {
            val addToCartRequest = AddToCartRequest(userId, productId)
            val response = productService.addToCart(addToCartRequest).body()

            if (response?.status == 200) {
                Resource.Success(response.message.orEmpty())
            } else {
                Resource.Fail(response?.message.orEmpty())
            }
        }

    suspend fun deleteFromCart(id: Int, userId: String): Resource<String> =
        withContext(Dispatchers.IO){
            try {
                val deleteFromCartRequest = DeleteFromCartRequest(userId, id)
                val response = productService.deleteFromCart(deleteFromCartRequest).body()

                if (response?.status == 200) {
                    Resource.Success(response.message.orEmpty())
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            }
            catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun clearCart(userId: String): Resource<String> =
        withContext(Dispatchers.IO){
            try {
                val clearCartRequest = ClearCartRequest(userId)
                val response = productService.clearCart(clearCartRequest).body()

                if(response?.status == 200){
                    Resource.Success(response.message.orEmpty())
                } else{
                    Resource.Fail(response?.message.orEmpty())
                }
            }
            catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun addToFavorites(product: ProductUI, userId: String) {
        productDao.addProduct(product.mapToProductEntity(userId))
    }

    suspend fun deleteFromFavorites(product: ProductUI, userId: String) {
        productDao.deleteProduct(product.mapToProductEntity(userId))
    }

    suspend fun getFavorites(userId: String): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val products = productDao.getProducts(userId)

                if (products.isEmpty()) {
                    Resource.Fail("Ürünler bulunamadı")
                } else {
                    Resource.Success(products.mapProductEntityToProductUI())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun clearFavorites(userId: String): Resource<String> =
        withContext(Dispatchers.IO) {
            try {
                productDao.clearFavorites(userId)
                Resource.Success("Favoriler başarıyla temizlendi")
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun getCategories(): Resource<List<String>> =
        withContext(Dispatchers.IO) {
            try {
                val response = productService.getCategories().body()
                if ( response?.status == 200) {
                    Resource.Success(response.categories.orEmpty())
                }
                else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }

    suspend fun getProductsByCategory(category: String, userId: String): Resource<List<ProductUI>> =
        withContext(Dispatchers.IO) {
            try {
                val favorites = productDao.getProductIds(userId)
                val response = productService.getProductsByCategory(category).body()

                if (response?.status == 200) {
                    Resource.Success(response.products.orEmpty().mapProductToProductUI(favorites))
                } else {
                    Resource.Fail(response?.message.orEmpty())
                }
            } catch (e: Exception) {
                Resource.Error(e.message.orEmpty())
            }
        }
}

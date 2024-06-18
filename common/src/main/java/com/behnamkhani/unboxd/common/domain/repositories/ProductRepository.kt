package com.behnamkhani.unboxd.common.domain.repositories

import com.behnamkhani.unboxd.common.domain.model.pagination.PaginatedProducts
import com.behnamkhani.unboxd.common.domain.model.product.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    suspend fun requestMoreProducts(limit: Int, skip: Int): PaginatedProducts
    suspend fun storeProducts(products: List<Product>)
    suspend fun getProductById(productId: Long): Product
    suspend fun isProductInWishlist(productId: Long): Boolean
    suspend fun removeProductFromWishlist(productId: Long)
    suspend fun insertProductIntoWishlist(productId: Long)
    fun getWishlist(): Flow<List<Product>>
    suspend fun searchProduct(query: String): Flow<List<Product>>
    suspend fun requestProductById(productId: Long): Product
    suspend fun insertProduct(product: Product)
    suspend fun isProductInLocalDb(productId: Long): Boolean
    suspend fun getNumberOfWishlistItems(): Int
}


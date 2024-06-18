package com.behnamkhani.unboxd.common.data.cache

import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedWishlist
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.relations.CachedProductAggregate
import kotlinx.coroutines.flow.Flow

interface Cache {
    fun getProducts(): Flow<List<CachedProductAggregate>>
    suspend fun storeProducts(products: List<CachedProductAggregate>)
    suspend fun getProductById(productId: Long): CachedProductAggregate
    suspend fun isProductInWishlist(productId: Long): Boolean
    suspend fun removeProductFromWishlist(productId: Long)
    suspend fun insertProductIntoWishlist(cachedWishlist: CachedWishlist)
    fun getWishlist():  Flow<List<CachedProductAggregate>>
    fun searchProducts(): Flow<List<CachedProductAggregate>>
    suspend fun storeProduct(product: CachedProductAggregate)
    suspend fun isProductInLocalDb(productId: Long): Boolean
    suspend fun getNumberOfWishlistItems(): Int
}
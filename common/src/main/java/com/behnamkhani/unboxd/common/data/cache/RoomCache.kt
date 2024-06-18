package com.behnamkhani.unboxd.common.data.cache

import com.behnamkhani.unboxd.common.data.cache.daos.ProductsDao
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedWishlist
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.relations.CachedProductAggregate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomCache @Inject constructor(
    private val productsDao: ProductsDao,
) : Cache {
    override fun getProducts(): Flow<List<CachedProductAggregate>> {
        return productsDao.getAllProductsWithWishlist()
    }

    override suspend fun storeProducts(products: List<CachedProductAggregate>) {
        productsDao.insertProduct(products)
    }

    override suspend fun getProductById(productId: Long): CachedProductAggregate {
        return productsDao.getProductById(productId)
    }

    override suspend fun isProductInWishlist(productId: Long): Boolean {
        return productsDao.isProductInWishlist(productId)
    }

    override suspend fun removeProductFromWishlist(productId: Long) {
        productsDao.removeWishlistByProductId(productId)
    }

    override suspend fun insertProductIntoWishlist(cachedWishlist: CachedWishlist) {
        productsDao.insertProductIntoInWishlist(cachedWishlist)
    }

    override fun getWishlist(): Flow<List<CachedProductAggregate>> {
        return productsDao.getWishlist()
    }

    override fun searchProducts(): Flow<List<CachedProductAggregate>> {

            return productsDao.searchProduct()
    }

    override suspend fun storeProduct(product: CachedProductAggregate) {
        productsDao.insertProductAggregate(product.product, product.images, product.tags, product.reviews, product.dimensions)
    }

    override suspend fun isProductInLocalDb(productId: Long): Boolean {
        return productsDao.isProductInLocalDb(productId)
    }

    override suspend fun getNumberOfWishlistItems(): Int {
        return productsDao.getNumberOfWishlistItems()
    }
}

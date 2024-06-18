package com.behnamkhani.unboxd.common.data.repositories


import com.behnamkhani.unboxd.common.data.api.ProductApi
import com.behnamkhani.unboxd.common.data.api.model.mappers.ApiPaginationMapper
import com.behnamkhani.unboxd.common.data.api.model.mappers.ApiProductMapper
import com.behnamkhani.unboxd.common.data.api.model.product.ApiPagination
import com.behnamkhani.unboxd.common.data.cache.Cache
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedWishlist
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.relations.CachedProductAggregate
import com.behnamkhani.unboxd.common.data.cache.model.mappers.CachedProductMapper
import com.behnamkhani.unboxd.common.domain.NetworkException
import com.behnamkhani.unboxd.common.domain.model.pagination.PaginatedProducts
import com.behnamkhani.unboxd.common.domain.model.product.Product
import com.behnamkhani.unboxd.common.domain.repositories.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject


class UnboxdProductRepository @Inject constructor(
    private val api: ProductApi,
    private val cache: Cache,
    private val apiProductMapper: ApiProductMapper,
    private val apiPaginationMapper: ApiPaginationMapper,
    private val cachedProductMapper: CachedProductMapper
) : ProductRepository {

    override fun getProducts(): Flow<List<Product>> {
        return cache.getProducts()
            .distinctUntilChanged()
            .map { productList ->
                productList.map {
                    it.product.toProductDomain(
                        images = it.images,
                        tags = it.tags,
                        reviews = it.reviews,
                        dimensions = it.dimensions,
                        isInWishlist = it.wishlist.isNotEmpty()

                    )
                }
            }
    }

    override suspend fun requestMoreProducts(limit: Int, skip: Int): PaginatedProducts {
        try {
            val (apiProducts, total) = api.getProducts(skip, limit)

            return PaginatedProducts(
                apiProducts.map { apiProductMapper.mapToDomain(it) },
                apiPaginationMapper.mapToDomain(ApiPagination(total, skip, limit))
            )
        } catch (exception: HttpException) {
            throw NetworkException(
                exception.message ?: "Code ${exception.code()}"
            )
        }
    }

    override suspend fun storeProducts(products: List<Product>) {
        cache.storeProducts(products.map { CachedProductAggregate.fromDomain(it) })
    }

    override suspend fun getProductById(productId: Long): Product {
        val productDetails = cache.getProductById(productId)
        return cachedProductMapper.mapToDomain(productDetails)
    }

    override suspend fun isProductInWishlist(productId: Long): Boolean {
        return cache.isProductInWishlist(productId)
    }

    override suspend fun removeProductFromWishlist(productId: Long) {
        return cache.removeProductFromWishlist(productId)
    }

    override suspend fun insertProductIntoWishlist(productId: Long) {
        return cache.insertProductIntoWishlist(CachedWishlist.fromDomain(productId))
    }

    override fun getWishlist(): Flow<List<Product>> {
        return cache.getWishlist()
            .map { productList ->
                productList.map {
                    it.product.toProductDomain(
                        images = it.images,
                        tags = it.tags,
                        reviews = it.reviews,
                        dimensions = it.dimensions,
                        isInWishlist = it.wishlist.isNotEmpty()
                    )
                }
            }
    }

    override suspend fun searchProduct(query: String): Flow<List<Product>> {
        try {
            return flowOf(api.searchProducts(query).products.map {
                apiProductMapper.mapToDomain(it)
            })

        } catch (exception: HttpException) {
            throw NetworkException(
                exception.message ?: "Code ${exception.code()}"
            )
        }
    }

    override suspend fun requestProductById(productId: Long): Product {
        val productDetails = api.requestProductById(productId)
        return apiProductMapper.mapToDomain(productDetails)
    }

    override suspend fun insertProduct(product: Product) {
        cache.storeProduct(CachedProductAggregate.fromDomain(product))
    }

    override suspend fun isProductInLocalDb(productId: Long): Boolean {
        return cache.isProductInLocalDb(productId)
    }

    override suspend fun getNumberOfWishlistItems(): Int {
        return cache.getNumberOfWishlistItems()
    }
}
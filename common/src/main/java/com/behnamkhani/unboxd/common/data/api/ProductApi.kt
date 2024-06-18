package com.behnamkhani.unboxd.common.data.api

import com.behnamkhani.unboxd.common.data.api.model.product.ApiPaginatedProduct
import com.behnamkhani.unboxd.common.data.api.model.product.ApiProduct
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {
    @GET(ApiConstants.PRODUCTS_ENDPOINT)
    suspend fun getProducts(
        @Query(ApiParameters.SKIP) skip: Int,
        @Query(ApiParameters.LIMIT) limit: Int,
    ): ApiPaginatedProduct

    @GET(ApiConstants.PRODUCTS_SEARCH_BY_QUERY_ENDPOINT)
    suspend fun searchProducts(
        @Query(ApiParameters.QUERY) query: String
    ): ApiPaginatedProduct


    @GET("${ApiConstants.PRODUCTS_ENDPOINT}/{productId}")
    suspend fun requestProductById(
        @Path("productId") productId: Long
    ): ApiProduct
}
package com.behnamkhani.unboxd.displayproduct.domain.usecases

import com.behnamkhani.unboxd.common.domain.NoMoreProductsException
import com.behnamkhani.unboxd.common.domain.model.pagination.Pagination
import com.behnamkhani.unboxd.common.domain.repositories.ProductRepository
import com.behnamkhani.unboxd.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RequestNextPageOfProducts @Inject constructor(
    private val productRepository: ProductRepository,
       private val dispatchersProvider: DispatchersProvider,
) {

    suspend operator fun invoke(
        skip: Int ,
        limit: Int = Pagination.DEFAULT_LIMIT,
    ): Pagination {
        return withContext(dispatchersProvider.io()) {
            val (products, pagination) =
                productRepository.requestMoreProducts(
                    limit,
                    skip
                )

            if (products.isEmpty()) {
                throw NoMoreProductsException("No more products to display!")
            }
            productRepository.storeProducts(products)
            return@withContext pagination
        }
    }
}
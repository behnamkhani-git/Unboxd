package com.behnamkhani.unboxd.productdetails.domain.usecases

import com.behnamkhani.unboxd.common.domain.model.product.Product
import com.behnamkhani.unboxd.common.domain.repositories.ProductRepository
import com.behnamkhani.unboxd.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RequestProductById @Inject constructor(
    private val productRepository: ProductRepository,
    private val dispatchersProvider: DispatchersProvider

) {
    suspend operator fun invoke(productId: Long): Product {
        return withContext(dispatchersProvider.io()) {
            return@withContext productRepository.requestProductById(productId)
        }
    }
}
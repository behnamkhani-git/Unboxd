package com.behnamkhani.unboxd.common.domain.usecases

import com.behnamkhani.unboxd.common.domain.model.product.Product
import com.behnamkhani.unboxd.common.domain.repositories.ProductRepository
import com.behnamkhani.unboxd.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject


class InsertProduct @Inject constructor(
    private val productRepository: ProductRepository,
    private val dispatchersProvider: DispatchersProvider,
) {
    suspend operator fun invoke(product: Product) =
        withContext(dispatchersProvider.io()) {
            productRepository.insertProduct(product)
        }
}
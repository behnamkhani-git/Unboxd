package com.behnamkhani.unboxd.wishlist.domain.usecases

import com.behnamkhani.unboxd.common.domain.repositories.ProductRepository
import com.behnamkhani.unboxd.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetNumberOfWishlistItems @Inject constructor(
    private val productRepository: ProductRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke() = withContext(dispatchersProvider.io()) {
        return@withContext productRepository.getNumberOfWishlistItems()
    }
}
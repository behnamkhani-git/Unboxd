package com.behnamkhani.unboxd.common.domain.usecases

import com.behnamkhani.unboxd.common.domain.repositories.ProductRepository
import com.behnamkhani.unboxd.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ToggleWishlist @Inject constructor(
    private val productRepository: ProductRepository,
    private val dispatchersProvider: DispatchersProvider,
) {
    suspend operator fun invoke(productId: Long) =
        withContext(dispatchersProvider.io()) {
            val wishlist: Boolean = productRepository.isProductInWishlist(productId)
            if (wishlist)
                productRepository.removeProductFromWishlist(productId)
            else
                productRepository.insertProductIntoWishlist(productId)
        }
}

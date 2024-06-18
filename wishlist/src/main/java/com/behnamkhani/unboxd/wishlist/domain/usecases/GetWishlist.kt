package com.behnamkhani.unboxd.wishlist.domain.usecases

import com.behnamkhani.unboxd.common.domain.repositories.ProductRepository
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

class GetWishlist @Inject constructor(
    private val productRepository: ProductRepository
){
    operator fun invoke() = productRepository.getWishlist()
        .filter { it.isNotEmpty() }
}
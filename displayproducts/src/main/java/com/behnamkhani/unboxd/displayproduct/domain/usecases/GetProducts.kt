package com.behnamkhani.unboxd.displayproduct.domain.usecases

import com.behnamkhani.unboxd.common.domain.repositories.ProductRepository
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

class GetProducts @Inject constructor(
    private val productRepository: ProductRepository){
    operator fun invoke() = productRepository.getProducts()
        .filter { it.isNotEmpty() }
}
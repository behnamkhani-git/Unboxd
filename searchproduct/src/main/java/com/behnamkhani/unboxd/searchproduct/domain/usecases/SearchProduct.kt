package com.behnamkhani.unboxd.searchproduct.domain.usecases

import com.behnamkhani.unboxd.common.domain.repositories.ProductRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject

class SearchProduct @Inject constructor(
    private val productRepository: ProductRepository
) {
    @OptIn(FlowPreview::class)
    suspend operator fun invoke(query: String) =
        productRepository.searchProduct(query)
            .debounce(1000)
}
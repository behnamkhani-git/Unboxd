package com.behnamkhani.unboxd.common.domain.model.pagination

import com.behnamkhani.unboxd.common.domain.model.product.Product

data class PaginatedProducts(
    val products: List<Product>,
    val pagination: Pagination
)

package com.behnamkhani.unboxd.common.domain.model.pagination

data class Pagination(
    val totalProducts: Int,
    val limit: Int,
    val skip: Int
){
    companion object {
        const val DEFAULT_LIMIT = 10
    }
}

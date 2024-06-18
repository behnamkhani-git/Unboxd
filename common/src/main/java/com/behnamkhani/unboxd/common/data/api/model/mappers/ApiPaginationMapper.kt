package com.behnamkhani.unboxd.common.data.api.model.mappers

import com.behnamkhani.unboxd.common.data.api.model.product.ApiPaginatedProduct
import com.behnamkhani.unboxd.common.data.api.model.product.ApiPagination
import com.behnamkhani.unboxd.common.domain.model.pagination.Pagination
import javax.inject.Inject

class ApiPaginationMapper @Inject constructor(): ApiMapper<ApiPagination?, Pagination> {

    override fun mapToDomain(apiEntity: ApiPagination?): Pagination {
        return Pagination(
            totalProducts = apiEntity?.total ?: 0,
            limit = apiEntity?.limit ?: 0,
            skip = apiEntity?.skip ?: 0
        )
    }
}

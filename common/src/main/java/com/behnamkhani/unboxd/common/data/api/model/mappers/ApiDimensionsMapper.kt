package com.behnamkhani.unboxd.common.data.api.model.mappers

import com.behnamkhani.unboxd.common.data.api.model.product.productdetails.ApiDimensions
import com.behnamkhani.unboxd.common.domain.model.product.productdetails.Dimensions
import javax.inject.Inject

class ApiDimensionsMapper @Inject constructor(): ApiMapper<ApiDimensions, Dimensions> {
    override fun mapToDomain(apiEntity: ApiDimensions): Dimensions {
        return Dimensions(
            apiEntity.width,
            apiEntity.height,
            apiEntity.depth
        )
    }
}
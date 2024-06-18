package com.behnamkhani.unboxd.common.data.api.model.product.productdetails

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiDimensions(
    @field:Json(name = "width") val width: Float,
    @field:Json(name = "height") val height: Float,
    @field:Json(name = "depth") val depth: Float
)

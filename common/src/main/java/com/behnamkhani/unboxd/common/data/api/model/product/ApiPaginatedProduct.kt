package com.behnamkhani.unboxd.common.data.api.model.product

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiPaginatedProduct(
    @field:Json(name = "products") val products: List<ApiProduct>,
    @field:Json(name = "total") val total: Int?,
    @field:Json(name = "skip") val skip: Int?,
    @field:Json(name = "limit") val limit: Int?
)

@JsonClass(generateAdapter = true)
data class ApiPagination(
    @field:Json(name = "total") val total: Int?,
    @field:Json(name = "skip") val skip: Int?,
    @field:Json(name = "limit") val limit: Int?
)




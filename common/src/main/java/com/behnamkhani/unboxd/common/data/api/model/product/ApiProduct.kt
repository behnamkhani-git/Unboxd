package com.behnamkhani.unboxd.common.data.api.model.product

import com.behnamkhani.unboxd.common.data.api.model.product.productdetails.ApiDimensions
import com.behnamkhani.unboxd.common.data.api.model.product.productdetails.ApiReview
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiProduct(
    @field:Json(name = "id") val id: Long?,
    @field:Json(name = "title") val title: String?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "category") val category: String?,
    @field:Json(name = "price") val price: Float?,
    @field:Json(name = "discountPercentage") val discountPercentage: Float?,
    @field:Json(name = "rating") val rating: Float?,
    @field:Json(name = "stock") val stock: Int?,
    @field:Json(name = "tags") val tags: List<String>?,
    @field:Json(name = "brand") val brand: String?,
    @field:Json(name = "dimensions") val dimensions: ApiDimensions?,
    @field:Json(name = "reviews") val reviews: List<ApiReview>?,
    @field:Json(name = "thumbnail") val thumbnail: String?,
    @field:Json(name = "images") val images: List<String>?,
)
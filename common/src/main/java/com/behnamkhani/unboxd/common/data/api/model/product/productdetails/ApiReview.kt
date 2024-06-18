package com.behnamkhani.unboxd.common.data.api.model.product.productdetails

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class ApiReview(
    @field:Json(name = "rating") val rating: Int,
    @field:Json(name = "comment") val comment: String,
    @field:Json(name = "reviewerName") val reviewerName:String,
    @field:Json(name = "reviewerEmail") val reviewerEmail: String,
)

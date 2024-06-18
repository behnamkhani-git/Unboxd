package com.behnamkhani.unboxd.common.domain.model.product.productdetails

data class Review(
    val rating: Int,
    val comment: String,
    val reviewerName:String,
    val reviewerEmail: String,
)

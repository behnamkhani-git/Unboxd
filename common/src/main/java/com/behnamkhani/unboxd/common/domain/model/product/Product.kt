package com.behnamkhani.unboxd.common.domain.model.product

import com.behnamkhani.unboxd.common.domain.model.product.productdetails.Dimensions
import com.behnamkhani.unboxd.common.domain.model.product.productdetails.Review

data class Product(
    val id: Long,
    val title: String,
    val description: String,
    val category: String,
    val price: Float,
    val discountPercentage: Float,
    val rating: Float,
    val stock: Int,
    val tags: List<String>,
    val brand: String,
    val dimensions: Dimensions,
    val reviews: List<Review>,
    val images: List<String>,
    val thumbnail: String,
    val isInWishlist: Boolean = false,
)

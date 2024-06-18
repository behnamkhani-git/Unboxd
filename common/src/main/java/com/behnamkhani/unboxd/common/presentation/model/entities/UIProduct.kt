package com.behnamkhani.unboxd.common.presentation.model.entities

import com.behnamkhani.unboxd.common.presentation.model.entities.productdetails.UIDimensions
import com.behnamkhani.unboxd.common.presentation.model.entities.productdetails.UIReview

data class UIProduct(
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
    val dimensions: UIDimensions,
    val reviews: List<UIReview>,
    val images: List<String>,
    val thumbnail: String,
    val isInWishlist: Boolean,
)

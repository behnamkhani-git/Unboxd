package com.behnamkhani.unboxd.productdetails.presentation.event

sealed class ProductDetailsEvent {
    data class LoadProductById(val productId: Long) : ProductDetailsEvent()
}
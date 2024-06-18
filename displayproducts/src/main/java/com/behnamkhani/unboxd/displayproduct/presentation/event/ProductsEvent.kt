package com.behnamkhani.unboxd.displayproduct.presentation.event

sealed class ProductsEvent {
    object RequestInitialProductsList : ProductsEvent()
    object RequestMoreProducts : ProductsEvent()
    data class ToggleWishlistForProduct(val productId: Long): ProductsEvent()
}
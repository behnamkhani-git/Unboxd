package com.behnamkhani.unboxd.wishlist.presentation.event

sealed class WishlistEvent {
    data class ToggleWishlistForProduct(val productId: Long): WishlistEvent()
}
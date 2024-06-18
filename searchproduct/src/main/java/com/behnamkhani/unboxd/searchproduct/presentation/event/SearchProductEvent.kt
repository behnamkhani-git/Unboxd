package com.behnamkhani.unboxd.searchproduct.presentation.event

sealed class SearchProductEvent {
    data class ToggleWishlistForProduct(val productId: Long, val isChecked: Boolean): SearchProductEvent()
    data class SearchByQuery(val query: String): SearchProductEvent()
}
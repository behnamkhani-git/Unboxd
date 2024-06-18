package com.behnamkhani.unboxd.wishlist.presentation.viewstate

import com.behnamkhani.unboxd.common.presentation.event.Event
import com.behnamkhani.unboxd.common.presentation.model.entities.UIProduct


data class WishlistViewState(
    val products: List<UIProduct> = emptyList(),
    val exception: Event<Throwable>? = null
)
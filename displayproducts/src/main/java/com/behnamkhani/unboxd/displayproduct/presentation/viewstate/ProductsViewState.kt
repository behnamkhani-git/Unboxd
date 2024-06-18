package com.behnamkhani.unboxd.displayproduct.presentation.viewstate

import com.behnamkhani.unboxd.common.presentation.event.Event
import com.behnamkhani.unboxd.common.presentation.model.entities.UIProduct

data class ProductsViewState(
    val loading: Boolean = true,
    val products: List<UIProduct> = emptyList(),
    val isProductListEmpty: Boolean = false,
    val exception: Event<Throwable>? = null
)
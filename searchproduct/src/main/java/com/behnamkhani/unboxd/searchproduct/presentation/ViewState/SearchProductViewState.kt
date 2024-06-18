package com.behnamkhani.unboxd.searchproduct.presentation.ViewState

import com.behnamkhani.unboxd.common.presentation.event.Event
import com.behnamkhani.unboxd.common.presentation.model.entities.UIProduct


data class SearchProductViewState(
    val products: List<UIProduct> = emptyList(),
    val exception: Event<Throwable>? = null
)
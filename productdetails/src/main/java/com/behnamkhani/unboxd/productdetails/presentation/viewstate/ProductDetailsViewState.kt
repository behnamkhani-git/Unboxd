package com.behnamkhani.unboxd.productdetails.presentation.viewstate

import com.behnamkhani.unboxd.common.presentation.event.Event
import com.behnamkhani.unboxd.common.presentation.model.entities.UIProduct
import com.behnamkhani.unboxd.common.presentation.model.entities.productdetails.UIDimensions

data class ProductDetailsViewState(
    val loading: Boolean = true,
    val numberOfReviews: String = "",
    val productDetails: UIProduct = UIProduct(
        0,
        "",
        "",
        "",
        0.0f,
        0.0f,
        0.0f,
        0,
        emptyList(),
        "",
        UIDimensions(0.0f,0.0f,0.0f), emptyList(), emptyList(),"", false)
    ,
    val exception: Event<Throwable>? = null
)



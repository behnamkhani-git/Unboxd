package com.behnamkhani.unboxd.common.presentation.model.mappers

import com.behnamkhani.unboxd.common.domain.model.product.Product
import com.behnamkhani.unboxd.common.presentation.model.entities.UIProduct
import com.behnamkhani.unboxd.common.presentation.model.entities.productdetails.UIDimensions
import com.behnamkhani.unboxd.common.presentation.model.entities.productdetails.UIReview
import javax.inject.Inject

class UiProductMapper @Inject constructor() : UIMapper<Product, UIProduct> {

    override fun mapToView(input: Product): UIProduct {
        return UIProduct(
            id = input.id,
            title = input.title,
            images = input.images,
            tags = input.tags,
            dimensions = UIDimensions(
                input.dimensions.width,
                input.dimensions.height,
                input.dimensions.depth
            ),
            rating = input.rating,
            reviews = input.reviews.map {
                UIReview(
                    it.rating,
                    it.comment,
                    it.reviewerName,
                    it.reviewerEmail
                )
            },
            thumbnail = input.thumbnail,
            brand = input.brand,
            category = input.category,
            description = input.description,
            price = input.price,
            stock = input.stock,
            discountPercentage = input.discountPercentage,
            isInWishlist = input.isInWishlist

        )
    }
}
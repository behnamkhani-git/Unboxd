package com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedImage
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.CachedProductWithDetails
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedDimensions
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedReview
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedTag
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedWishlist
import com.behnamkhani.unboxd.common.domain.model.product.Product

data class CachedProductAggregate(
    @Embedded
    val product: CachedProductWithDetails,

    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val images: List<CachedImage>,

    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val reviews: List<CachedReview>,

    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val dimensions: CachedDimensions,

    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val wishlist: List<CachedWishlist>,

    @Relation(
        parentColumn = "productId",
        entityColumn = "tag",
        associateBy = Junction(CachedProductTagCrossRef::class)
    )
    val tags: List<CachedTag>,


    ) {

    companion object {
        fun fromDomain(productWithDetails: Product): CachedProductAggregate {
            return CachedProductAggregate(
                product = CachedProductWithDetails.fromDomain(productWithDetails),

                images = productWithDetails.images.map {
                    CachedImage.fromDomain(productWithDetails.id, it)
                },

                reviews = productWithDetails.reviews.map {
                    CachedReview.fromDomain(productWithDetails.id, it)
                },

                dimensions = CachedDimensions.fromDomain(
                    productWithDetails.id,
                    productWithDetails.dimensions
                ),

                tags = productWithDetails.tags.map { CachedTag(it) },

                wishlist = emptyList()
            )
        }
    }
}

package com.behnamkhani.unboxd.common.data.cache.model.cachedproduct

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedDimensions
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedImage
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedReview
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedTag
import com.behnamkhani.unboxd.common.domain.model.product.Product
import com.behnamkhani.unboxd.common.domain.model.product.productdetails.Dimensions
import com.behnamkhani.unboxd.common.domain.model.product.productdetails.Review

@Entity(
    tableName = "products",
)
data class CachedProductWithDetails(
    @PrimaryKey(autoGenerate = true) val productId: Long = 0,
    val title: String,
    val description: String,
    val category: String,
    val price: Float,
    val discountPercentage: Float,
    val rating: Float,
    val stock: Int,
    val brand: String,
    val thumbnail: String,
) {
    fun toProductDomain(images: List<CachedImage>, tags: List<CachedTag>, reviews: List<CachedReview>, dimensions: CachedDimensions, isInWishlist: Boolean): Product {
        return Product(
            id = productId,
            title = title,
            description = description,
            category = category,
            price = price,
            discountPercentage = discountPercentage,
            rating = rating,
            stock = stock,
            tags = tags.map { it.tag },
            brand = brand,
            dimensions = mapDimensions(dimensions),
            reviews = mapReviews(reviews),
            images = images.map { it.image },
            thumbnail = thumbnail,
            isInWishlist = isInWishlist,
        )
    }

    private fun mapReviews(reviews: List<CachedReview>): List<Review> {
        return reviews.map {
            it.toDomain()
        }
    }

    private fun mapDimensions(dimensions: CachedDimensions): Dimensions {
        return Dimensions(dimensions.width, dimensions.height, dimensions.depth)
    }

    companion object {
        fun fromDomain(domainModel: Product): CachedProductWithDetails {
            return CachedProductWithDetails(
                productId = domainModel.id,
                title = domainModel.title,
                description = domainModel.description,
                category = domainModel.category,
                price = domainModel.price,
                discountPercentage = domainModel.discountPercentage,
                rating = domainModel.rating,
                stock = domainModel.stock,
                brand = domainModel.brand,
                thumbnail = domainModel.thumbnail,
            )
        }
    }
}
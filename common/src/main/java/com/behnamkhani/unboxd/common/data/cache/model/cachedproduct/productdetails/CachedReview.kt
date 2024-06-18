package com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.CachedProductWithDetails
import com.behnamkhani.unboxd.common.domain.model.product.productdetails.Review

@Entity(
    tableName = "reviews",
    foreignKeys = [
        ForeignKey(
            entity = CachedProductWithDetails::class,
            parentColumns = ["productId"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("productId")]
)
data class CachedReview(
    @PrimaryKey(autoGenerate = true)
    val reviewId: Long = 0,
    val productId: Long = 0,
    val rating: Int,
    val comment: String,
    val reviewerName: String,
    val reviewerEmail: String,
) {
    fun toDomain(): Review = Review(rating, comment, reviewerName, reviewerEmail)
    companion object {
        fun fromDomain(productId: Long, review: Review): CachedReview {
            return CachedReview(
                productId = productId,
                rating = review.rating,
                comment = review.comment,
                reviewerName = review.reviewerName,
                reviewerEmail = review.reviewerEmail
            )
        }
    }
}


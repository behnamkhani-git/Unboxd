package com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.CachedProductWithDetails

@Entity(
    tableName = "wishlist",
    foreignKeys = [
        ForeignKey(
            entity = CachedProductWithDetails::class,
            parentColumns = ["productId"],
            childColumns = ["productId"],
        )
    ],
    indices = [Index("productId")]
)
data class CachedWishlist(
    @PrimaryKey
    val productId: Long = 0,

) {
    companion object {
        fun fromDomain(productId: Long): CachedWishlist {
            return CachedWishlist(productId)
        }
    }
}
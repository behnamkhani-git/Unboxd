package com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.CachedProductWithDetails

@Entity(
    tableName = "images",
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
data class CachedImage(
    @PrimaryKey(autoGenerate = true)
    val imageId: Long = 0,
    val productId: Long,
    val image: String,
) {
    companion object {
        fun fromDomain(productId: Long, image: String): CachedImage {
            return CachedImage(
                productId = productId,
                image = image
            )
        }
    }
}
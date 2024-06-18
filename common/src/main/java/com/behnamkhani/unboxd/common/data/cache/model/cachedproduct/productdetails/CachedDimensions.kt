package com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.CachedProductWithDetails
import com.behnamkhani.unboxd.common.domain.model.product.productdetails.Dimensions

@Entity(
    tableName = "dimensions",
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
data class CachedDimensions(
    @PrimaryKey(autoGenerate = true)
    val dimensionsId: Long = 0,
    val productId: Long = 0,
    val width: Float,
    val height: Float,
    val depth: Float
) {
    companion object {
        fun fromDomain(
            productId: Long,
            dimensions: Dimensions
        ): CachedDimensions {
            return CachedDimensions(
                productId = productId,
                width = dimensions.width,
                height = dimensions.height,
                depth = dimensions.depth
            )
        }
    }
}

package com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.relations

import androidx.room.Entity

@Entity(
    tableName = "product_tag_cross_ref",
    primaryKeys = ["productId", "tag"],
)
data class CachedProductTagCrossRef(
    val productId: Long,
    val tag: String
)
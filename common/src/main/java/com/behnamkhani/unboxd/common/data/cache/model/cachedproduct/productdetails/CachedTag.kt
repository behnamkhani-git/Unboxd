package com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.CachedProductWithDetails

@Entity(
    tableName = "tags"
)
data class CachedTag(
    @PrimaryKey()
    val tag: String,
)




package com.behnamkhani.unboxd.common.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.behnamkhani.unboxd.common.data.cache.daos.ProductsDao
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedImage
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.CachedProductWithDetails
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedDimensions
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedReview
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedTag
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedWishlist
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.relations.CachedProductTagCrossRef

@Database(
    entities = [
        CachedImage::class,
        CachedProductWithDetails::class,
        CachedReview::class,
        CachedDimensions::class,
        CachedTag::class,
        CachedProductTagCrossRef::class,
        CachedWishlist::class
    ],
    version = 1
)

abstract class UnboxdDatabase : RoomDatabase() {
    abstract fun productsDao(): ProductsDao
}
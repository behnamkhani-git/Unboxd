package com.behnamkhani.unboxd.common.data.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedImage
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedDimensions
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedReview
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedTag
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.relations.CachedProductAggregate
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.relations.CachedProductTagCrossRef
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.CachedProductWithDetails
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedWishlist
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProductsDao {

    @Transaction
    @Query("SELECT p.*, w.productId as wishlistId FROM products as p LEFT JOIN wishlist w ON p.productId = w.productId")
    abstract fun getAllProductsWithWishlist(): Flow<List<CachedProductAggregate>>

    @Transaction
    @Query("SELECT p.*, w.productId as wishlistId FROM products as p INNER JOIN wishlist as w ON p.productId = w.productId")
    abstract fun getWishlist(): Flow<List<CachedProductAggregate>>

    suspend fun insertProduct(productAggregates: List<CachedProductAggregate>) {

        for (productAggregate in productAggregates) {
            for (tag in productAggregate.tags) {
                insertProductTagCrossRef(
                    CachedProductTagCrossRef(
                        productAggregate.product.productId,
                        tag.tag
                    )
                )
            }
        }
        for (productAggregate in productAggregates) {
            insertProductAggregate(
                productAggregate.product,
                productAggregate.images,
                productAggregate.tags,
                productAggregate.reviews,
                productAggregate.dimensions,
            )
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertProductTagCrossRef(crossRef: CachedProductTagCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertProductAggregate(
        product: CachedProductWithDetails,
        images: List<CachedImage>,
        tags: List<CachedTag>,
        reviews: List<CachedReview>,
        dimensions: CachedDimensions,
    )

    @Transaction
    @Query("SELECT * FROM products WHERE productId = :productId")
    abstract suspend fun getProductById(productId: Long): CachedProductAggregate

    @Transaction
    @Query("SELECT * FROM wishlist WHERE productId = :productId")
    abstract suspend fun getWishlistByProductId(productId: Long): CachedWishlist

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertProductIntoInWishlist(wishlist: CachedWishlist)

    @Transaction
    @Query("DELETE FROM wishlist WHERE productId = :productId")
    abstract suspend fun removeWishlistByProductId(productId: Long)

    @Transaction
    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE productId = :productId)")
    abstract suspend fun isProductInWishlist(productId: Long): Boolean

    @Transaction
    @Query("SELECT p.*, w.productId as wishlistId FROM products as p LEFT JOIN wishlist w ON p.productId = w.productId")
    abstract fun searchProduct(): Flow<List<CachedProductAggregate>>

    @Transaction
    @Query("SELECT EXISTS(SELECT 1 FROM products WHERE productId = :productId)")
    abstract suspend fun isProductInLocalDb(productId: Long): Boolean

    @Transaction
    @Query("SELECT COUNT(*) FROM wishlist")
    abstract fun getNumberOfWishlistItems(): Int
}

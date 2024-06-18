package com.behnamkhani.unboxd.common.data.cache.model.mappers

import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.productdetails.CachedDimensions
import com.behnamkhani.unboxd.common.data.cache.model.cachedproduct.relations.CachedProductAggregate
import com.behnamkhani.unboxd.common.domain.model.product.Product
import com.behnamkhani.unboxd.common.domain.model.product.productdetails.Dimensions
import javax.inject.Inject

class CachedProductMapper @Inject constructor(): CachedMapper<CachedProductAggregate, Product> {
    override fun mapToDomain(
        cachedModel: CachedProductAggregate,
    ): Product {
        return Product(
            id = cachedModel.product.productId ,
            title = cachedModel.product.title,
            description = cachedModel.product.description,
            price = cachedModel.product.price,
            discountPercentage = cachedModel.product.discountPercentage,
            rating = cachedModel.product.rating,
            stock = cachedModel.product.stock,
            brand = cachedModel.product.brand,
            category = cachedModel.product.category,
            thumbnail = cachedModel.product.thumbnail,
            images = cachedModel.images.map { it.image },
            tags = cachedModel.tags.map { it.tag },
            dimensions = mapDimensions(cachedModel.dimensions),
            reviews = cachedModel.reviews.map { it.toDomain() },
        )
    }

    private fun mapDimensions(dimensions: CachedDimensions): Dimensions {
        return Dimensions(dimensions.width, dimensions.height, dimensions.depth)
    }
}
package com.behnamkhani.unboxd.common.data.api.model.mappers


import com.behnamkhani.unboxd.common.data.api.model.product.ApiProduct
import com.behnamkhani.unboxd.common.data.api.model.product.productdetails.ApiDimensions
import com.behnamkhani.unboxd.common.domain.model.product.Product
import javax.inject.Inject

class ApiProductMapper @Inject constructor(
    private val apiDimensionsMapper: ApiDimensionsMapper,
    private val apiReviewMapper: ApiReviewMapper
) : ApiMapper<ApiProduct, Product> {

    override fun mapToDomain(apiEntity: ApiProduct): Product {
        return Product(
            id = apiEntity.id ?: throw MappingException("Product ID cannot be null!"),
            title = apiEntity.title.orEmpty(),
            description = apiEntity.description.orEmpty(),
            price = apiEntity.price ?: 0.0f,
            discountPercentage = apiEntity.discountPercentage ?: 0.0f,
            rating = apiEntity.rating ?: 0.0f,
            stock = apiEntity.stock ?: 0,
            brand = apiEntity.brand.orEmpty(),
            category = apiEntity.category.orEmpty(),
            thumbnail = apiEntity.thumbnail.orEmpty(),
            images = apiEntity.images.orEmpty(),
            tags = apiEntity.tags.orEmpty(),
            dimensions = apiDimensionsMapper.mapToDomain(apiEntity.dimensions ?: ApiDimensions(0.0f, 0.0f, 0.0f)),
            reviews = apiReviewMapper.mapToDomain(apiEntity.reviews.orEmpty()),
        )
    }
}
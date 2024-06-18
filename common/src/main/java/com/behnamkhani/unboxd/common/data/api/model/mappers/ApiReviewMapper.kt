package com.behnamkhani.unboxd.common.data.api.model.mappers

import com.behnamkhani.unboxd.common.data.api.model.product.productdetails.ApiReview
import com.behnamkhani.unboxd.common.domain.model.product.productdetails.Review
import javax.inject.Inject

class ApiReviewMapper @Inject constructor(): ApiMapper<List<ApiReview>, List<Review>> {
    override fun mapToDomain(apiEntity: List<ApiReview>): List<Review> {
        return apiEntity.map {
            Review(it.rating, it.comment, it.reviewerName, it.reviewerEmail)
        }
    }
}
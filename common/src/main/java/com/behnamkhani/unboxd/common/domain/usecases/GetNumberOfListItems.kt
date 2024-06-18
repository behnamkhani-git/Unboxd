package com.behnamkhani.unboxd.common.domain.usecases

import com.behnamkhani.unboxd.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetNumberOfListItems @Inject constructor(
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke(reviews: List<Any>) = withContext(dispatchersProvider.io()) {
        return@withContext reviews.size
    }
}
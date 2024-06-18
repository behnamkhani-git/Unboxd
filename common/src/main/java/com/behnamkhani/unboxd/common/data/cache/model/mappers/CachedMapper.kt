package com.behnamkhani.unboxd.common.data.cache.model.mappers

interface CachedMapper<E, D> {
    fun mapToDomain(cachedModel: E): D
}
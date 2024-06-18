package com.behnamkhani.unboxd.common.presentation.model.mappers

interface UIMapper<E, V> {

    fun mapToView(input: E): V
}
package com.behnamkhani.unboxd.common.presentation.event

data class Event<out T>(private val content: T) {

    private var hasBeenHandled = false
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}
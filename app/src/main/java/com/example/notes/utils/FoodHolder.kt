package com.example.notes.utils

sealed class FoodHolder<T>(
    val data: T? = null,
    val throwable: Throwable? = null
) {
    class Start<T>: FoodHolder<T>()

    class TextChange<T>: FoodHolder<T>()

    class Loading<T>: FoodHolder<T>()

    class Success<T>(data: T?): FoodHolder<T>(data = data)

    class Error<T>(throwable: Throwable?): FoodHolder<T>(throwable = throwable)
}
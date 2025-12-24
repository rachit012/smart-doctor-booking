package com.example.frontened.common

sealed class ResultState<out T> {
    data class Error<T> (val message: String): ResultState<T>()
    data class Success<T>(val data: T): ResultState<T>()
    data object Loading: ResultState<Nothing>()
}
package me.deema.sdk.data.util

sealed interface Response<out T> {
    class Success<T>(val data: T) : Response<T>
    class Error<T>(val errorMessage: String) : Response<T>
}
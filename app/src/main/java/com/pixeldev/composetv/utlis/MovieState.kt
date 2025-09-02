package com.pixeldev.composetv.utlis

sealed class MovieState<out T> {
    object Idle : MovieState<Nothing>()
    object Loading : MovieState<Nothing>()
    data class Success<out T>(val data: T) : MovieState<T>()
    data class Error(val message: String) : MovieState<Nothing>()
    data class TokenExpired(val message: String) : MovieState<Nothing>()

}
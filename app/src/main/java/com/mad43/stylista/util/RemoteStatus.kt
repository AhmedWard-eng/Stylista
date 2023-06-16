package com.mad43.stylista.util

sealed class RemoteStatus<out T> {

    class Success<T>(val data: T) : RemoteStatus<T>()
    class Failure(val msg: Throwable) : RemoteStatus<Nothing>()
    data class Valied<T>(val message: Int) : RemoteStatus<T>()
    object Loading : RemoteStatus<Nothing>()

}

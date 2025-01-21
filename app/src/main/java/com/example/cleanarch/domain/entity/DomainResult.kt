package com.example.cleanarch.domain.entity

sealed class DomainResult<out T> {
    data class Success<out T>(val data: T) : DomainResult<T>()
    data class Error(val exception: Throwable) : DomainResult<Nothing>()
    object Loading : DomainResult<Nothing>()
}
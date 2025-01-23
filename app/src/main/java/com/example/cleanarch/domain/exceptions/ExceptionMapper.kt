package com.example.cleanarch.domain.exceptions

interface ExceptionMapper {
    fun map(exception: Throwable): Throwable
}
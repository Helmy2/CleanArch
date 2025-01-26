package com.example.domain.exceptions

interface ExceptionMapper {
    fun map(throwable: Throwable): Throwable
}
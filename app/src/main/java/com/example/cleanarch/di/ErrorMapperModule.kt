package com.example.cleanarch.di

import com.example.cleanarch.domain.exceptions.AuthExceptionMapper
import com.example.cleanarch.domain.exceptions.ExceptionMapper
import org.koin.dsl.module

val exceptionMapperModule = module {
    single<ExceptionMapper> { AuthExceptionMapper() }
}
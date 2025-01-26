package com.example.data.exception

import com.example.data.exception.AuthException.AccountConversionFailedException
import com.example.data.exception.AuthException.AnonymousSignInFailedException
import com.example.data.exception.AuthException.EmailAlreadyInUseException
import com.example.data.exception.AuthException.GenericAuthException
import com.example.data.exception.AuthException.InvalidCredentialsException
import com.example.data.exception.AuthException.NoInternetConnectionException
import com.example.data.exception.AuthException.UserNotFoundException
import com.example.data.exception.AuthException.WeakPasswordException
import com.example.domain.exceptions.ExceptionMapper
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

sealed class AuthException(message: String) : Exception(message) {
    class EmailAlreadyInUseException : AuthException("Email already in use")
    class WeakPasswordException : AuthException("Password too weak")
    class InvalidCredentialsException : AuthException("Invalid credentials")
    class NoInternetConnectionException : AuthException("No internet connection")
    class GenericAuthException : AuthException("Authentication failed")
    class AnonymousSignInFailedException : AuthException("Anonymous sign-in failed")
    class UserNotFoundException : Exception("User not found")
    class AccountConversionFailedException : Exception("Account conversion failed")
}

class AuthExceptionMapper : ExceptionMapper {
    override fun map(throwable: Throwable): Throwable = when (throwable) {
        is FirebaseAuthUserCollisionException -> EmailAlreadyInUseException()
        is FirebaseAuthWeakPasswordException -> WeakPasswordException()
        is FirebaseAuthInvalidCredentialsException -> InvalidCredentialsException()
        is FirebaseNetworkException -> NoInternetConnectionException()
        is AnonymousSignInFailedException -> AnonymousSignInFailedException()
        is UserNotFoundException -> UserNotFoundException()
        is AccountConversionFailedException -> AccountConversionFailedException()
        else -> GenericAuthException()
    }
}





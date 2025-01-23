package com.example.cleanarch.domain.exceptions

import com.example.cleanarch.domain.exceptions.AuthException.EmailAlreadyInUseException
import com.example.cleanarch.domain.exceptions.AuthException.GenericAuthException
import com.example.cleanarch.domain.exceptions.AuthException.InvalidCredentialsException
import com.example.cleanarch.domain.exceptions.AuthException.NoInternetConnectionException
import com.example.cleanarch.domain.exceptions.AuthException.WeakPasswordException
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
    class UserNotFoundException() : Exception("User not found")
    class AccountConversionFailedException() : Exception("Account conversion failed")
}

class AuthExceptionMapper : ExceptionMapper {
    override fun map(throwable: Throwable): Throwable = when (throwable) {
        is FirebaseAuthUserCollisionException -> EmailAlreadyInUseException()
        is FirebaseAuthWeakPasswordException -> WeakPasswordException()
        is FirebaseAuthInvalidCredentialsException -> InvalidCredentialsException()
        is FirebaseNetworkException -> NoInternetConnectionException()
        else -> GenericAuthException()
    }
}





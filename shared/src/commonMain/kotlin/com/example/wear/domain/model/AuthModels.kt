package com.example.wear.domain.model

/** Помилки валідації полів форми (текст підбирається в UI за локалізацією). */
enum class ValidationError {
    UsernameEmpty,
    UsernameTooShort,
    UsernameTooLong,
    UsernameInvalidChars,
    PasswordEmpty,
    PasswordTooShort,
    PasswordTooLong,
    PasswordNeedsLetterAndDigit,
    ConfirmationEmpty,
    PasswordsDoNotMatch
}

/** Помилки автентифікації. */
enum class AuthError {
    InvalidCredentials,
    UserAlreadyExists
}

sealed interface AuthResult {
    data class Success(val username: String) : AuthResult
    data class Failure(val error: AuthError) : AuthResult
}

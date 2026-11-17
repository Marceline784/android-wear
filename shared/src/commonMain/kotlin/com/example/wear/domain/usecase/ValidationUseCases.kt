package com.example.wear.domain.usecase

import com.example.wear.domain.model.ValidationError

/** Ім'я користувача: 3-24 символи, літери (в т.ч. кирилиця), цифри, крапка, дефіс, підкреслення. */
class ValidateUsernameUseCase {
    operator fun invoke(username: String): ValidationError? {
        val value = username.trim()
        return when {
            value.isEmpty() -> ValidationError.UsernameEmpty
            value.length < MIN_LENGTH -> ValidationError.UsernameTooShort
            value.length > MAX_LENGTH -> ValidationError.UsernameTooLong
            !value.all { it.isLetterOrDigit() || it == '.' || it == '-' || it == '_' } ->
                ValidationError.UsernameInvalidChars
            else -> null
        }
    }

    companion object {
        const val MIN_LENGTH = 3
        const val MAX_LENGTH = 24
    }
}

/** Пароль: 8-64 символи, щонайменше одна літера і одна цифра. */
class ValidatePasswordUseCase {
    operator fun invoke(password: String): ValidationError? = when {
        password.isEmpty() -> ValidationError.PasswordEmpty
        password.length < MIN_LENGTH -> ValidationError.PasswordTooShort
        password.length > MAX_LENGTH -> ValidationError.PasswordTooLong
        !(password.any { it.isLetter() } && password.any { it.isDigit() }) ->
            ValidationError.PasswordNeedsLetterAndDigit
        else -> null
    }

    companion object {
        const val MIN_LENGTH = 8
        const val MAX_LENGTH = 64
    }
}

/** Підтвердження пароля має збігатися з паролем. */
class ValidatePasswordConfirmationUseCase {
    operator fun invoke(password: String, confirmation: String): ValidationError? = when {
        confirmation.isEmpty() -> ValidationError.ConfirmationEmpty
        confirmation != password -> ValidationError.PasswordsDoNotMatch
        else -> null
    }
}

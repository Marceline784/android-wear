package com.example.wear.ui.auth.register

import com.example.wear.domain.model.AuthError
import com.example.wear.domain.model.ValidationError

data class RegisterState(
    val username: String = "",
    val password: String = "",
    val confirmation: String = "",
    val usernameError: ValidationError? = null,
    val passwordError: ValidationError? = null,
    val confirmationError: ValidationError? = null,
    val authError: AuthError? = null,
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val showErrors: Boolean = false
)

sealed interface RegisterIntent {
    data class UsernameChanged(val value: String) : RegisterIntent
    data class PasswordChanged(val value: String) : RegisterIntent
    data class ConfirmationChanged(val value: String) : RegisterIntent
    data object TogglePasswordVisibility : RegisterIntent
    data object Submit : RegisterIntent
    data object OpenLogin : RegisterIntent
}

sealed interface RegisterEffect {
    data class Registered(val username: String) : RegisterEffect
    data object OpenLogin : RegisterEffect
}

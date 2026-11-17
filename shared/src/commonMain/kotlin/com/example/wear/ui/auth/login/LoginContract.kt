package com.example.wear.ui.auth.login

import com.example.wear.domain.model.AuthError
import com.example.wear.domain.model.ValidationError

data class LoginState(
    val username: String = "",
    val password: String = "",
    val usernameError: ValidationError? = null,
    val passwordError: ValidationError? = null,
    val authError: AuthError? = null,
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    /** Після першої спроби входу помилки валідації оновлюються під час введення. */
    val showErrors: Boolean = false,
    /** Користувач щойно зареєструвався: показуємо підказку й підставляємо його ім'я. */
    val justRegistered: Boolean = false
)

sealed interface LoginIntent {
    data class UsernameChanged(val value: String) : LoginIntent
    data class PasswordChanged(val value: String) : LoginIntent
    data object TogglePasswordVisibility : LoginIntent
    data object Submit : LoginIntent
    data object OpenRegister : LoginIntent
    data class PrefillAfterRegistration(val username: String) : LoginIntent
}

sealed interface LoginEffect {
    data object SignedIn : LoginEffect
    data object OpenRegister : LoginEffect
}

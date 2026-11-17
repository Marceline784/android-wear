package com.example.wear.ui.auth.register

import androidx.lifecycle.viewModelScope
import com.example.wear.core.AppLog
import com.example.wear.core.mvi.MviViewModel
import com.example.wear.domain.model.AuthResult
import com.example.wear.domain.usecase.RegisterUseCase
import com.example.wear.domain.usecase.ValidatePasswordConfirmationUseCase
import com.example.wear.domain.usecase.ValidatePasswordUseCase
import com.example.wear.domain.usecase.ValidateUsernameUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterViewModel(
    private val validateUsername: ValidateUsernameUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val validateConfirmation: ValidatePasswordConfirmationUseCase,
    private val registerUseCase: RegisterUseCase
) : MviViewModel<RegisterState, RegisterIntent, RegisterEffect>(RegisterState()) {

    override fun onIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.UsernameChanged -> setState {
                copy(
                    username = intent.value,
                    usernameError = if (showErrors) validateUsername(intent.value) else null,
                    authError = null
                )
            }
            is RegisterIntent.PasswordChanged -> setState {
                copy(
                    password = intent.value,
                    passwordError = if (showErrors) validatePassword(intent.value) else null,
                    // підтвердження залежить від пароля, тому перевіряємо його теж
                    confirmationError = if (showErrors) validateConfirmation(intent.value, confirmation) else null,
                    authError = null
                )
            }
            is RegisterIntent.ConfirmationChanged -> setState {
                copy(
                    confirmation = intent.value,
                    confirmationError = if (showErrors) validateConfirmation(password, intent.value) else null,
                    authError = null
                )
            }
            RegisterIntent.TogglePasswordVisibility -> setState { copy(passwordVisible = !passwordVisible) }
            RegisterIntent.Submit -> submit()
            RegisterIntent.OpenLogin -> sendEffect(RegisterEffect.OpenLogin)
        }
    }

    private fun submit() {
        val s = currentState
        if (s.isLoading) return

        val usernameError = validateUsername(s.username)
        val passwordError = validatePassword(s.password)
        val confirmationError = validateConfirmation(s.password, s.confirmation)
        if (usernameError != null || passwordError != null || confirmationError != null) {
            setState {
                copy(
                    showErrors = true,
                    usernameError = usernameError,
                    passwordError = passwordError,
                    confirmationError = confirmationError
                )
            }
            return
        }

        setState {
            copy(
                isLoading = true, showErrors = true, authError = null,
                usernameError = null, passwordError = null, confirmationError = null
            )
        }
        viewModelScope.launch {
            val result = withContext(Dispatchers.Default) { registerUseCase(s.username, s.password) }
            when (result) {
                is AuthResult.Success -> {
                    AppLog.i("Account created for ${result.username}")
                    setState { copy(isLoading = false) }
                    sendEffect(RegisterEffect.Registered(result.username))
                }
                is AuthResult.Failure -> {
                    AppLog.w("Registration failed: ${result.error}")
                    setState { copy(isLoading = false, authError = result.error) }
                }
            }
        }
    }
}

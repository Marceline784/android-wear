package com.example.wear.ui.auth.login

import androidx.lifecycle.viewModelScope
import com.example.wear.core.AppLog
import com.example.wear.core.mvi.MviViewModel
import com.example.wear.domain.model.AuthResult
import com.example.wear.domain.usecase.LoginUseCase
import com.example.wear.domain.usecase.ValidatePasswordUseCase
import com.example.wear.domain.usecase.ValidateUsernameUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val validateUsername: ValidateUsernameUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val loginUseCase: LoginUseCase
) : MviViewModel<LoginState, LoginIntent, LoginEffect>(LoginState()) {

    override fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.UsernameChanged -> setState {
                copy(
                    username = intent.value,
                    usernameError = if (showErrors) validateUsername(intent.value) else null,
                    authError = null
                )
            }
            is LoginIntent.PasswordChanged -> setState {
                copy(
                    password = intent.value,
                    passwordError = if (showErrors) validatePassword(intent.value) else null,
                    authError = null
                )
            }
            LoginIntent.TogglePasswordVisibility -> setState { copy(passwordVisible = !passwordVisible) }
            LoginIntent.Submit -> submit()
            LoginIntent.OpenRegister -> sendEffect(LoginEffect.OpenRegister)
            is LoginIntent.PrefillAfterRegistration ->
                setState { copy(username = intent.username, justRegistered = true) }
        }
    }

    private fun submit() {
        val s = currentState
        if (s.isLoading) return

        val usernameError = validateUsername(s.username)
        val passwordError = validatePassword(s.password)
        if (usernameError != null || passwordError != null) {
            setState { copy(showErrors = true, usernameError = usernameError, passwordError = passwordError) }
            return
        }

        setState {
            copy(isLoading = true, showErrors = true, usernameError = null, passwordError = null, authError = null)
        }
        viewModelScope.launch {
            val result = withContext(Dispatchers.Default) { loginUseCase(s.username, s.password) }
            when (result) {
                is AuthResult.Success -> {
                    AppLog.i("Sign in succeeded for ${result.username}")
                    setState { copy(isLoading = false) }
                    sendEffect(LoginEffect.SignedIn)
                }
                is AuthResult.Failure -> {
                    AppLog.w("Sign in failed: ${result.error}")
                    setState { copy(isLoading = false, authError = result.error) }
                }
            }
        }
    }
}

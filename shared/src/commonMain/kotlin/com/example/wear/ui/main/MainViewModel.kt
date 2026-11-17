package com.example.wear.ui.main

import com.example.wear.core.AppLog
import com.example.wear.core.mvi.MviViewModel
import com.example.wear.domain.usecase.GetCurrentUserUseCase
import com.example.wear.domain.usecase.IsOnboardingCompletedUseCase
import com.example.wear.domain.usecase.LogoutUseCase
import com.example.wear.domain.usecase.ResetOnboardingUseCase

/**
 * ViewModel головного екрана. Перевіряє, чи користувач пройшов онбординг і чи виконав вхід,
 * та повідомляє UI, на який екран перейти.
 */
class MainViewModel(
    private val isOnboardingCompleted: IsOnboardingCompletedUseCase,
    private val resetOnboarding: ResetOnboardingUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val logout: LogoutUseCase
) : MviViewModel<MainState, MainIntent, MainEffect>(MainState()) {

    init {
        checkStartDestination()
    }

    override fun onIntent(intent: MainIntent) {
        when (intent) {
            MainIntent.ResetOnboarding -> {
                resetOnboarding()
                AppLog.i("Onboarding flag reset")
                sendEffect(MainEffect.OpenOnboarding)
            }
            MainIntent.SignOut -> {
                logout()
                AppLog.i("User signed out")
                sendEffect(MainEffect.OpenLogin)
            }
        }
    }

    private fun checkStartDestination() {
        val completed = isOnboardingCompleted()
        val user = getCurrentUser()
        AppLog.i("Start check: onboardingCompleted=$completed, signedIn=${user != null}")
        when {
            !completed -> sendEffect(MainEffect.OpenOnboarding)
            user == null -> sendEffect(MainEffect.OpenLogin)
            else -> setState { copy(isChecking = false, onboardingCompleted = true, userName = user) }
        }
    }
}

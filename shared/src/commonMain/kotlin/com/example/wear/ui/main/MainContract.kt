package com.example.wear.ui.main

data class MainState(
    /** Триває перевірка, який екран показати (онбординг, вхід чи головний). */
    val isChecking: Boolean = true,
    val onboardingCompleted: Boolean = false,
    val userName: String? = null
)

sealed interface MainIntent {
    data object ResetOnboarding : MainIntent
    data object SignOut : MainIntent
}

sealed interface MainEffect {
    data object OpenOnboarding : MainEffect
    data object OpenLogin : MainEffect
}

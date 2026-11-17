package com.example.wear.ui.onboarding

import com.example.wear.core.AppLog
import com.example.wear.core.mvi.MviViewModel
import com.example.wear.domain.usecase.CompleteOnboardingUseCase
import com.example.wear.domain.usecase.GetCurrentUserUseCase

class OnboardingViewModel(
    private val completeOnboarding: CompleteOnboardingUseCase,
    private val getCurrentUser: GetCurrentUserUseCase
) : MviViewModel<OnboardingState, OnboardingIntent, OnboardingEffect>(OnboardingState()) {

    override fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            OnboardingIntent.Skip -> finish("skipped")
            OnboardingIntent.Finish -> finish("completed")
        }
    }

    private fun finish(how: String) {
        if (currentState.isSaving) return
        setState { copy(isSaving = true) }
        completeOnboarding()
        AppLog.i("Onboarding $how, result saved")
        sendEffect(OnboardingEffect.Completed(signedIn = getCurrentUser() != null))
    }
}

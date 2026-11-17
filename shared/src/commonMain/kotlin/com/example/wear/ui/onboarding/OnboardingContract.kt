package com.example.wear.ui.onboarding

data class OnboardingState(val isSaving: Boolean = false)

sealed interface OnboardingIntent {
    /** Користувач пропустив онбординг. */
    data object Skip : OnboardingIntent

    /** Користувач дійшов до кінця і натиснув «Почати». */
    data object Finish : OnboardingIntent
}

sealed interface OnboardingEffect {
    /** Результат збережено. [signedIn]: користувач уже увійшов, тоді вхід не потрібен. */
    data class Completed(val signedIn: Boolean) : OnboardingEffect
}

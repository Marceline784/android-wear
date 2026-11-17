package com.example.wear.data

import com.example.wear.domain.repository.OnboardingRepository
import com.russhwolf.settings.Settings

class SettingsOnboardingRepository(private val settings: Settings) : OnboardingRepository {

    override fun isCompleted(): Boolean = settings.getBoolean(KEY_ONBOARDING_COMPLETED, false)

    override fun setCompleted(completed: Boolean) {
        settings.putBoolean(KEY_ONBOARDING_COMPLETED, completed)
    }

    private companion object {
        // Той самий ключ, що був у UserSettings (Лаб. 4): збережений прапорець не втрачається
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }
}

package com.example.wear.core

import com.russhwolf.settings.Settings

/**
 * Налаштування користувача (бібліотека multiplatform-settings).
 * Android: SharedPreferences, Desktop: java.util.prefs. Значення зберігаються між запусками.
 */
class UserSettings(private val settings: Settings = Settings()) {

    /** Чи користувач уже пройшов екран ознайомлення (онбординг). */
    var onboardingCompleted: Boolean
        get() = settings.getBoolean(KEY_ONBOARDING_COMPLETED, false)
        set(value) = settings.putBoolean(KEY_ONBOARDING_COMPLETED, value)

    /** Мова інтерфейсу: "system" (за пристроєм), "en" або "uk". */
    var language: String
        get() = settings.getString(KEY_LANGUAGE, LANGUAGE_SYSTEM)
        set(value) = settings.putString(KEY_LANGUAGE, value)

    /** Чутливість до холоду 0..2 (1.0 = помірна). Збігається з ColdSensitivity у профілі на бекенді. */
    var coldSensitivity: Float
        get() = settings.getFloat(KEY_COLD_SENSITIVITY, 1f)
        set(value) = settings.putFloat(KEY_COLD_SENSITIVITY, value)

    /** Світла тема (true) чи темна (false). */
    var lightMode: Boolean
        get() = settings.getBoolean(KEY_LIGHT_MODE, true)
        set(value) = settings.putBoolean(KEY_LIGHT_MODE, value)

    /** Чи ввімкнений слот гардероба (top, bottom, shoes, bags, accessories) для рекомендацій. */
    fun isSlotEnabled(slot: String): Boolean = settings.getBoolean("slot_$slot", true)

    fun setSlotEnabled(slot: String, enabled: Boolean) = settings.putBoolean("slot_$slot", enabled)

    private companion object {
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        const val KEY_LANGUAGE = "language"
        const val KEY_COLD_SENSITIVITY = "cold_sensitivity"
        const val KEY_LIGHT_MODE = "light_mode"
        const val LANGUAGE_SYSTEM = "system"
    }
}

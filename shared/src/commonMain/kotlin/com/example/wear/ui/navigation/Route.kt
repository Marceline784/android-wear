package com.example.wear.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/** Екрани застосунку (ключі Navigation 3). */
@Serializable
sealed interface Route : NavKey {
    @Serializable data object Main : Route
    @Serializable data object Onboarding : Route
    /** [registeredUsername] заповнюється після реєстрації: поле імені підставляється автоматично. */
    @Serializable data class Login(val registeredUsername: String? = null) : Route
    @Serializable data object Register : Route
}

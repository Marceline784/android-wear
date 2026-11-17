package com.example.wear

import com.example.wear.core.security.Sha256PasswordHasher
import com.example.wear.data.SettingsAuthRepository
import com.example.wear.data.SettingsOnboardingRepository
import com.example.wear.domain.model.AuthError
import com.example.wear.domain.model.AuthResult
import com.example.wear.domain.usecase.CompleteOnboardingUseCase
import com.example.wear.domain.usecase.IsOnboardingCompletedUseCase
import com.example.wear.domain.usecase.ResetOnboardingUseCase
import com.russhwolf.settings.MapSettings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AuthRepositoryTest {

    private fun repo(settings: MapSettings = MapSettings()) = SettingsAuthRepository(settings, Sha256PasswordHasher())

    @Test
    fun registerThenLogin() {
        val settings = MapSettings()
        val auth = repo(settings)

        assertEquals(AuthResult.Success("Anna"), auth.register("Anna", "Secret123"))
        // реєстрація не виконує вхід
        assertNull(auth.currentUser())

        // ім'я не залежить від регістру, повертається так, як було при реєстрації
        assertEquals(AuthResult.Success("Anna"), auth.login("anna", "Secret123"))
        assertEquals("Anna", auth.currentUser())
    }

    @Test
    fun wrongPasswordAndUnknownUserGiveTheSameError() {
        val auth = repo()
        auth.register("anna", "Secret123")

        assertEquals(AuthResult.Failure(AuthError.InvalidCredentials), auth.login("anna", "Wrong12345"))
        assertEquals(AuthResult.Failure(AuthError.InvalidCredentials), auth.login("nobody", "Secret123"))
        assertNull(auth.currentUser())
    }

    @Test
    fun duplicateRegistrationIsRejected() {
        val auth = repo()
        auth.register("anna", "Secret123")
        assertEquals(AuthResult.Failure(AuthError.UserAlreadyExists), auth.register("ANNA", "Other12345"))
    }

    @Test
    fun passwordIsNotStoredInPlainText() {
        val settings = MapSettings()
        repo(settings).register("anna", "Secret123")
        settings.keys.forEach { key ->
            assertFalse(settings.getStringOrNull(key)?.contains("Secret123") ?: false, "plain password in $key")
        }
    }

    @Test
    fun sessionSurvivesNewRepositoryInstance() {
        val settings = MapSettings()
        repo(settings).register("anna", "Secret123")
        repo(settings).login("anna", "Secret123")
        assertEquals("anna", repo(settings).currentUser())
    }

    @Test
    fun onboardingUseCases() {
        val repository = SettingsOnboardingRepository(MapSettings())
        val isCompleted = IsOnboardingCompletedUseCase(repository)
        val complete = CompleteOnboardingUseCase(repository)
        val reset = ResetOnboardingUseCase(repository)

        assertFalse(isCompleted())
        complete()
        assertTrue(isCompleted())
        reset()
        assertFalse(isCompleted())
    }
}

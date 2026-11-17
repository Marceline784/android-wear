package com.example.wear

import com.example.wear.domain.model.ValidationError
import com.example.wear.domain.usecase.ValidatePasswordConfirmationUseCase
import com.example.wear.domain.usecase.ValidatePasswordUseCase
import com.example.wear.domain.usecase.ValidateUsernameUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ValidationTest {
    private val username = ValidateUsernameUseCase()
    private val password = ValidatePasswordUseCase()
    private val confirmation = ValidatePasswordConfirmationUseCase()

    @Test
    fun usernameRules() {
        assertEquals(ValidationError.UsernameEmpty, username("   "))
        assertEquals(ValidationError.UsernameTooShort, username("ab"))
        assertEquals(ValidationError.UsernameTooLong, username("a".repeat(25)))
        assertEquals(ValidationError.UsernameInvalidChars, username("anna kov"))
        assertEquals(ValidationError.UsernameInvalidChars, username("anna@mail"))
        assertNull(username("anna.kov-2_x"))
        assertNull(username("Анна_К"))
        assertNull(username("  anna  "))
    }

    @Test
    fun passwordRules() {
        assertEquals(ValidationError.PasswordEmpty, password(""))
        assertEquals(ValidationError.PasswordTooShort, password("abc123"))
        assertEquals(ValidationError.PasswordTooLong, password("a1".repeat(33)))
        assertEquals(ValidationError.PasswordNeedsLetterAndDigit, password("onlyletters"))
        assertEquals(ValidationError.PasswordNeedsLetterAndDigit, password("12345678"))
        assertNull(password("Secret123"))
        assertNull(password("парольЧотири4"))
    }

    @Test
    fun confirmationRules() {
        assertEquals(ValidationError.ConfirmationEmpty, confirmation("Secret123", ""))
        assertEquals(ValidationError.PasswordsDoNotMatch, confirmation("Secret123", "Secret124"))
        assertNull(confirmation("Secret123", "Secret123"))
    }
}

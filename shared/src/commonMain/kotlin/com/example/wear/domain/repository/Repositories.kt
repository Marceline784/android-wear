package com.example.wear.domain.repository

import com.example.wear.domain.model.AuthResult

/** Прапорець проходження онбордингу (зберігається через multiplatform-settings). */
interface OnboardingRepository {
    fun isCompleted(): Boolean
    fun setCompleted(completed: Boolean)
}

/** Реєстрація, вхід і поточна сесія користувача. */
interface AuthRepository {
    /** Створює обліковий запис. Не виконує вхід: сесія з'являється лише після [login]. */
    fun register(username: String, password: String): AuthResult
    fun login(username: String, password: String): AuthResult
    fun logout()

    /** Ім'я поточного користувача або null, якщо вхід не виконано. */
    fun currentUser(): String?
}

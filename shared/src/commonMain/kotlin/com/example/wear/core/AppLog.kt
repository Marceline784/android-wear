package com.example.wear.core

import co.touchlab.kermit.Logger

/**
 * Єдине місце логування застосунку (бібліотека Kermit).
 * Працює однаково на Android (Logcat) і Desktop (консоль).
 * Правила: у логи не пишемо паролів, токенів та інших секретів.
 */
object AppLog {
    private val log = Logger.withTag("Wearcast")

    fun d(message: String) = log.d { message }
    fun i(message: String) = log.i { message }
    fun w(message: String) = log.w { message }
    fun e(message: String, throwable: Throwable? = null) = log.e(throwable) { message }
}

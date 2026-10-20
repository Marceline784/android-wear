package com.example.wear.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.platform.LocalLayoutDirection
import java.util.Locale

actual object LocalAppLocale {
    private var default: Locale? = null

    actual val current: String
        @Composable get() = Locale.getDefault().toString()

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        if (default == null) default = Locale.getDefault()
        val new = if (value == null) default!! else Locale.forLanguageTag(value)
        Locale.setDefault(new)
        return LocalLayoutDirection.provides(LocalLayoutDirection.current)
    }
}

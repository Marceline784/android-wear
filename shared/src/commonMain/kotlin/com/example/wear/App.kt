package com.example.wear

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.wear.core.AppLog
import com.example.wear.core.LocalAppLocale
import com.example.wear.core.UserSettings
import com.example.wear.ui.main.MainScreen
import com.example.wear.ui.main.MainTab
import com.example.wear.ui.onboarding.OnboardingScreen
import com.example.wear.ui.theme.WearTheme

/** Точка входу: онбординг (Pager) -> головний екран з нижнім меню. Мова перемикається "на льоту". */
@Composable
fun App() {
    val settings = remember { UserSettings() }
    var onboardingDone by remember { mutableStateOf(settings.onboardingCompleted) }
    var lightMode by remember { mutableStateOf(settings.lightMode) }
    var language by remember { mutableStateOf(settings.language) }   // "system" | "en" | "uk"
    var tab by remember { mutableStateOf(MainTab.Today) }            // тут, щоб не скидалась при зміні мови

    LaunchedEffect(Unit) {
        AppLog.i("App started, onboardingCompleted=$onboardingDone, language=$language")
    }

    val appLocale = when (language) {
        "uk" -> "uk"
        "en" -> "en"
        else -> null
    }

    CompositionLocalProvider(LocalAppLocale provides appLocale) {
        // key(language) змушує перебудувати UI з новими рядками після зміни мови
        key(language) {
            WearTheme(darkTheme = !lightMode) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    if (!onboardingDone) {
                        OnboardingScreen(
                            onFinish = {
                                settings.onboardingCompleted = true
                                onboardingDone = true
                                AppLog.i("Onboarding finished, flag saved")
                            }
                        )
                    } else {
                        MainScreen(
                            settings = settings,
                            tab = tab,
                            onTabChange = { tab = it },
                            lightMode = lightMode,
                            onLightModeChange = {
                                lightMode = it
                                settings.lightMode = it
                                AppLog.i("Light mode changed to $it")
                            },
                            language = language,
                            onLanguageChange = {
                                settings.language = it
                                language = it
                                AppLog.i("Language changed to $it")
                            },
                            onboardingCompleted = onboardingDone,
                            onOnboardingCompletedChange = {
                                onboardingDone = it
                                settings.onboardingCompleted = it
                                AppLog.i("onboardingCompleted changed to $it")
                            }
                        )
                    }
                }
            }
        }
    }
}

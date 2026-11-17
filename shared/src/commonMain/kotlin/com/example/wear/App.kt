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
import com.example.wear.di.appModule
import com.example.wear.ui.main.MainTab
import com.example.wear.ui.navigation.AppNavigation
import com.example.wear.ui.navigation.rememberAppBackStack
import com.example.wear.ui.theme.WearTheme
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.dsl.koinConfiguration

@Composable
fun App() {
    // Koin: єдине місце, де запускається впровадження залежностей
    KoinApplication(configuration = koinConfiguration { modules(appModule) }) {
        AppContent()
    }
}

@Composable
private fun AppContent() {
    val settings = koinInject<UserSettings>()
    var lightMode by remember { mutableStateOf(settings.lightMode) }
    var language by remember { mutableStateOf(settings.language) }   // "system" | "en" | "uk"
    var tab by remember { mutableStateOf(MainTab.Today) }            // тут, щоб не скидалась при зміні мови

    // Стек Navigation 3 живе поза key(language): зміна мови не скидає навігацію
    val backStack = rememberAppBackStack()

    LaunchedEffect(Unit) {
        AppLog.i("App started, language=$language")
    }

    val appLocale = when (language) {
        "uk" -> "uk"
        "en" -> "en"
        else -> null
    }

    CompositionLocalProvider(LocalAppLocale provides appLocale) {
        key(language) {
            WearTheme(darkTheme = !lightMode) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavigation(
                        backStack = backStack,
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
                        }
                    )
                }
            }
        }
    }
}

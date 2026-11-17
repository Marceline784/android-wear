package com.example.wear.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wear.core.UserSettings
import com.example.wear.core.mvi.ObserveEffects
import org.koin.compose.viewmodel.koinViewModel

/** Точка входу головного екрана: ViewModel вирішує, чи показувати головний екран, онбординг або вхід. */
@Composable
fun MainRoute(
    settings: UserSettings,
    tab: MainTab,
    onTabChange: (MainTab) -> Unit,
    lightMode: Boolean,
    onLightModeChange: (Boolean) -> Unit,
    language: String,
    onLanguageChange: (String) -> Unit,
    onOpenOnboarding: () -> Unit,
    onOpenLogin: () -> Unit,
    viewModel: MainViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEffects(viewModel.effects) { effect ->
        when (effect) {
            MainEffect.OpenOnboarding -> onOpenOnboarding()
            MainEffect.OpenLogin -> onOpenLogin()
        }
    }

    if (state.isChecking) {
        // Короткий проміжок, поки ViewModel вирішує, куди перейти: лише фон теми
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))
    } else {
        MainScreen(
            settings = settings,
            tab = tab,
            onTabChange = onTabChange,
            lightMode = lightMode,
            onLightModeChange = onLightModeChange,
            language = language,
            onLanguageChange = onLanguageChange,
            onboardingCompleted = state.onboardingCompleted,
            userName = state.userName,
            onResetOnboarding = { viewModel.onIntent(MainIntent.ResetOnboarding) },
            onSignOut = { viewModel.onIntent(MainIntent.SignOut) }
        )
    }
}

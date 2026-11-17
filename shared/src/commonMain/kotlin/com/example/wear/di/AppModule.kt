package com.example.wear.di

import com.example.wear.core.UserSettings
import com.example.wear.core.security.PasswordHasher
import com.example.wear.core.security.Sha256PasswordHasher
import com.example.wear.data.SettingsAuthRepository
import com.example.wear.data.SettingsOnboardingRepository
import com.example.wear.domain.repository.AuthRepository
import com.example.wear.domain.repository.OnboardingRepository
import com.example.wear.domain.usecase.CompleteOnboardingUseCase
import com.example.wear.domain.usecase.GetCurrentUserUseCase
import com.example.wear.domain.usecase.IsOnboardingCompletedUseCase
import com.example.wear.domain.usecase.LoginUseCase
import com.example.wear.domain.usecase.LogoutUseCase
import com.example.wear.domain.usecase.RegisterUseCase
import com.example.wear.domain.usecase.ResetOnboardingUseCase
import com.example.wear.domain.usecase.ValidatePasswordConfirmationUseCase
import com.example.wear.domain.usecase.ValidatePasswordUseCase
import com.example.wear.domain.usecase.ValidateUsernameUseCase
import com.example.wear.ui.auth.login.LoginViewModel
import com.example.wear.ui.auth.register.RegisterViewModel
import com.example.wear.ui.main.MainViewModel
import com.example.wear.ui.onboarding.OnboardingViewModel
import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/** Усі залежності застосунку (Koin). */
val appModule = module {
    // Сховище налаштувань (multiplatform-settings)
    single<Settings> { Settings() }
    single { UserSettings(get()) }
    single<PasswordHasher> { Sha256PasswordHasher() }

    // Репозиторії
    single<OnboardingRepository> { SettingsOnboardingRepository(get()) }
    single<AuthRepository> { SettingsAuthRepository(get(), get()) }

    // Варіанти використання (use cases)
    factoryOf(::CompleteOnboardingUseCase)
    factoryOf(::IsOnboardingCompletedUseCase)
    factoryOf(::ResetOnboardingUseCase)
    factoryOf(::LoginUseCase)
    factoryOf(::RegisterUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::GetCurrentUserUseCase)
    factoryOf(::ValidateUsernameUseCase)
    factoryOf(::ValidatePasswordUseCase)
    factoryOf(::ValidatePasswordConfirmationUseCase)

    // ViewModel-и екранів
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
}

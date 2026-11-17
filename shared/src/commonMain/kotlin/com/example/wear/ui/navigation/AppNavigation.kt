package com.example.wear.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.wear.core.UserSettings
import com.example.wear.ui.auth.login.LoginRoute
import com.example.wear.ui.auth.register.RegisterRoute
import com.example.wear.ui.main.MainRoute
import com.example.wear.ui.main.MainTab
import com.example.wear.ui.onboarding.OnboardingRoute
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

// Реєстрація ключів для збереження стеку (потрібна для Android після повороту екрана та для не-JVM платформ)
private val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Route.Main::class, Route.Main.serializer())
            subclass(Route.Onboarding::class, Route.Onboarding.serializer())
            subclass(Route.Login::class, Route.Login.serializer())
            subclass(Route.Register::class, Route.Register.serializer())
        }
    }
}

/**
 * Стек навігації користувача (Navigation 3). Стартовий екран: Main. Він сам перевіряє через MainViewModel,
 * чи пройдено онбординг і чи виконано вхід, і за потреби перекидає на Onboarding або Login.
 */
@Composable
fun rememberAppBackStack(): MutableList<NavKey> = rememberNavBackStack(navConfig, Route.Main)

@Composable
fun AppNavigation(
    backStack: MutableList<NavKey>,
    settings: UserSettings,
    tab: MainTab,
    onTabChange: (MainTab) -> Unit,
    lightMode: Boolean,
    onLightModeChange: (Boolean) -> Unit,
    language: String,
    onLanguageChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    /** Замінює весь стек одним екраном (після онбордингу, входу, виходу). */
    fun replaceWith(route: Route) {
        backStack.clear()
        backStack.add(route)
    }

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()   // окремий ViewModel для кожного екрана стеку
        ),
        entryProvider = entryProvider {
            entry<Route.Main> {
                MainRoute(
                    settings = settings,
                    tab = tab,
                    onTabChange = onTabChange,
                    lightMode = lightMode,
                    onLightModeChange = onLightModeChange,
                    language = language,
                    onLanguageChange = onLanguageChange,
                    onOpenOnboarding = { replaceWith(Route.Onboarding) },
                    onOpenLogin = { replaceWith(Route.Login()) }
                )
            }
            entry<Route.Onboarding> {
                // Онбординг завершено -> екран входу (якщо вже є активний вхід, одразу головний екран)
                OnboardingRoute(
                    onCompleted = { signedIn -> replaceWith(if (signedIn) Route.Main else Route.Login()) }
                )
            }
            entry<Route.Login> { key ->
                LoginRoute(
                    registeredUsername = key.registeredUsername,
                    onSignedIn = { replaceWith(Route.Main) },
                    onOpenRegister = { backStack.add(Route.Register) }
                )
            }
            entry<Route.Register> {
                RegisterRoute(
                    // Після реєстрації користувач потрапляє на екран входу й входить сам
                    onRegistered = { username -> replaceWith(Route.Login(registeredUsername = username)) },
                    onOpenLogin = { replaceWith(Route.Login()) }
                )
            }
        }
    )
}

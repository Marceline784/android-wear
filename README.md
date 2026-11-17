# Wearcast

Kotlin Multiplatform (Android + Desktop), Compose Multiplatform. Підбір одягу з гардероба за погодою.

## Запуск

- Android: `./gradlew :androidApp:assembleDebug` або Run у Android Studio
- Desktop: `./gradlew :desktopApp:run`
- Тести: `./gradlew :shared:jvmTest` і `./gradlew :shared:testAndroidHostTest`

## Лабораторна №5: навігація, DI, MVI, авторизація

| Пункт завдання | Де реалізовано |
|---|---|
| Navigation 3 | `ui/navigation/` (Route, AppNavigation), залежність `navigation3-ui` (KMP-артефакт охоплює й Android) |
| Koin | `di/AppModule.kt`, запуск у `App.kt` (`KoinApplication`) |
| multiplatform-settings | `data/` (репозиторії), `core/UserSettings.kt` |
| MVI онбордингу + use case збереження | `ui/onboarding/` (State, Intent, Effect, ViewModel), `domain/usecase/OnboardingUseCases.kt` |
| Екран авторизації, перехід з онбордингу | `ui/auth/`, ефект `OnboardingEffect.Completed` -> `Route.Login` |
| ViewModel і use case для головного екрана | `ui/main/MainViewModel.kt`, `IsOnboardingCompletedUseCase` |
| Вибір екрана за результатом онбордингу | `MainViewModel`: онбординг не пройдено -> Onboarding, не увійшов -> Login, інакше Main |
| Дизайн і MVI екрана авторизації | `ui/auth/login/`, `ui/auth/register/`, `ui/auth/components/` |
| Валідація імені та пароля | `domain/usecase/ValidationUseCases.kt` |
| Дії клавіатури | Ім'я: Next -> фокус на пароль; Пароль: Done -> вхід (як кнопка "Увійти") |
| Реєстрація і вхід | `data/AuthRepositoryImpl.kt`: сіль + SHA-256, пароль у відкритому вигляді не зберігається |

### Потік екранів

```
Main (перевірка) -> онбординг не пройдено -> Onboarding -> Login
                 -> не увійшов            -> Login
                 -> увійшов               -> Main (вкладки)

Login -> "Створити" -> Register -> знову Login (ім'я підставлене) -> Main
```

Як у звичайних застосунках: спершу вхід; немає облікового запису, створюєте його й знову входите.
Поки користувач не натиснув "Вийти", пароль повторно не запитується: сесія зберігається між запусками.

### Логотип

Угорі екранів входу й реєстрації по центру стоять логотип і назва (`brand_name` у `strings.xml`).
Власний логотип: `shared/src/commonMain/kotlin/com/example/wear/ui/auth/components/BrandLogo.kt`.

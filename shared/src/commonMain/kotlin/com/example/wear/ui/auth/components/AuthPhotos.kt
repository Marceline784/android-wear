package com.example.wear.ui.auth.components

import org.jetbrains.compose.resources.DrawableResource

/*
 * ФОТО ДЛЯ ЕКРАНІВ ВХОДУ ТА РЕЄСТРАЦІЇ
 *
 * Поки значення null, на екранах показується рамка-підказка з назвою файлу.
 * Як додати своє фото:
 *   1. Покладіть файл у shared/src/commonMain/composeResources/drawable/
 *      (для входу: auth_login.jpg, для реєстрації: auth_register.jpg; рекомендовано 1200 x 900 px, JPG).
 *   2. Замініть null нижче на Res.drawable.auth_login / Res.drawable.auth_register
 *      і додайте імпорт: import wear.shared.generated.resources.*
 *   3. Збудуйте проєкт (Gradle згенерує Res.drawable.auth_login).
 */
val LoginPhoto: DrawableResource? = null
val RegisterPhoto: DrawableResource? = null

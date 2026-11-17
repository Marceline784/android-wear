package com.example.wear.ui.auth.components

import org.jetbrains.compose.resources.DrawableResource

/*
 * ВЛАСНИЙ ЛОГОТИП (необов'язково)
 *
 * Поки значення null, на екранах входу та реєстрації показується значок вішака з теми застосунку.
 * Щоб поставити свій логотип:
 *   1. Покладіть файл logo.png (квадрат, наприклад 256 x 256) у
 *      shared/src/commonMain/composeResources/drawable/
 *   2. Замініть null нижче на Res.drawable.logo
 *      і додайте імпорт: import wear.shared.generated.resources.*
 */
val BrandLogo: DrawableResource? = null

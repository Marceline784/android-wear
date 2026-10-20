package com.example.wear.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import wear.shared.generated.resources.Res
import wear.shared.generated.resources.manrope_bold
import wear.shared.generated.resources.manrope_medium
import wear.shared.generated.resources.manrope_regular
import wear.shared.generated.resources.manrope_semibold
import wear.shared.generated.resources.playfair_display_bold
import wear.shared.generated.resources.playfair_display_semibold

/**
 * Шрифти: заголовки Playfair Display, інтерфейс Manrope.
 * Обидва підтримують кирилицю, тож українська локалізація виглядатиме так само гарно.
 */
@Composable
fun wearTypography(): Typography {
    val heading = FontFamily(
        Font(Res.font.playfair_display_semibold, FontWeight.SemiBold),
        Font(Res.font.playfair_display_bold, FontWeight.Bold)
    )
    val body = FontFamily(
        Font(Res.font.manrope_regular, FontWeight.Normal),
        Font(Res.font.manrope_medium, FontWeight.Medium),
        Font(Res.font.manrope_semibold, FontWeight.SemiBold),
        Font(Res.font.manrope_bold, FontWeight.Bold)
    )

    val base = Typography()
    return Typography(
        displayLarge = base.displayLarge.copy(fontFamily = heading, fontWeight = FontWeight.Bold),
        displayMedium = base.displayMedium.copy(fontFamily = heading, fontWeight = FontWeight.Bold),
        displaySmall = base.displaySmall.copy(fontFamily = heading, fontWeight = FontWeight.SemiBold),
        headlineLarge = base.headlineLarge.copy(fontFamily = heading, fontWeight = FontWeight.SemiBold),
        headlineMedium = base.headlineMedium.copy(fontFamily = heading, fontWeight = FontWeight.SemiBold),
        headlineSmall = base.headlineSmall.copy(fontFamily = heading, fontWeight = FontWeight.SemiBold),
        titleLarge = base.titleLarge.copy(fontFamily = heading, fontWeight = FontWeight.SemiBold),
        titleMedium = base.titleMedium.copy(fontFamily = body, fontWeight = FontWeight.SemiBold),
        titleSmall = base.titleSmall.copy(fontFamily = body, fontWeight = FontWeight.SemiBold),
        bodyLarge = base.bodyLarge.copy(fontFamily = body),
        bodyMedium = base.bodyMedium.copy(fontFamily = body),
        bodySmall = base.bodySmall.copy(fontFamily = body),
        labelLarge = base.labelLarge.copy(fontFamily = body, fontWeight = FontWeight.SemiBold),
        labelMedium = base.labelMedium.copy(fontFamily = body, fontWeight = FontWeight.Medium),
        labelSmall = base.labelSmall.copy(fontFamily = body, fontWeight = FontWeight.Medium)
    )
}

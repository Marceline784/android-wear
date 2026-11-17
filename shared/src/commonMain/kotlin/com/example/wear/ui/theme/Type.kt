package com.example.wear.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import wear.shared.generated.resources.Res
import wear.shared.generated.resources.open_sans_bold
import wear.shared.generated.resources.open_sans_medium
import wear.shared.generated.resources.open_sans_regular
import wear.shared.generated.resources.open_sans_semibold
import wear.shared.generated.resources.unbounded_bold
import wear.shared.generated.resources.unbounded_medium
import wear.shared.generated.resources.unbounded_regular
import wear.shared.generated.resources.unbounded_semibold

/**
 * Шрифти: заголовки й акценти Unbounded, звичайний текст Open Sans.
 * Обидва підтримують кирилицю, тож українська локалізація виглядає так само, як англійська.
 * Unbounded широкий, тому розміри заголовків трохи менші за стандартні Material 3.
 */
@Composable
fun wearTypography(): Typography {
    val heading = FontFamily(
        Font(Res.font.unbounded_regular, FontWeight.Normal),
        Font(Res.font.unbounded_medium, FontWeight.Medium),
        Font(Res.font.unbounded_semibold, FontWeight.SemiBold),
        Font(Res.font.unbounded_bold, FontWeight.Bold)
    )
    val body = FontFamily(
        Font(Res.font.open_sans_regular, FontWeight.Normal),
        Font(Res.font.open_sans_medium, FontWeight.Medium),
        Font(Res.font.open_sans_semibold, FontWeight.SemiBold),
        Font(Res.font.open_sans_bold, FontWeight.Bold)
    )

    /** Заголовочний стиль: Unbounded із заданим розміром і пропорційною висотою рядка. */
    fun TextStyle.heading(size: TextUnit, weight: FontWeight) = copy(
        fontFamily = heading,
        fontWeight = weight,
        fontSize = size,
        lineHeight = size * 1.3f,
        letterSpacing = 0.sp
    )

    val base = Typography()
    return Typography(
        displayLarge = base.displayLarge.heading(46.sp, FontWeight.SemiBold),
        displayMedium = base.displayMedium.heading(38.sp, FontWeight.SemiBold),
        displaySmall = base.displaySmall.heading(30.sp, FontWeight.Medium),
        headlineLarge = base.headlineLarge.heading(25.sp, FontWeight.Medium),
        headlineMedium = base.headlineMedium.heading(21.sp, FontWeight.Medium),
        headlineSmall = base.headlineSmall.heading(18.sp, FontWeight.Medium),
        titleLarge = base.titleLarge.heading(17.sp, FontWeight.Medium),
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

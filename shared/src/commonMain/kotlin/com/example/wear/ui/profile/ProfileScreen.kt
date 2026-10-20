package com.example.wear.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wear.core.AppLog
import com.example.wear.core.LocalAppLocale
import com.example.wear.core.UserSettings
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import wear.shared.generated.resources.*
import kotlin.math.roundToInt

private data class SlotInfo(
    val key: String,
    val icon: DrawableResource,
    val title: StringResource,
    val description: StringResource
)

private val slots = listOf(
    SlotInfo("top", Res.drawable.ic_outfits, Res.string.slot_top, Res.string.slot_top_desc),
    SlotInfo("bottom", Res.drawable.ic_pants, Res.string.slot_bottom, Res.string.slot_bottom_desc),
    SlotInfo("shoes", Res.drawable.ic_shoe, Res.string.slot_shoes, Res.string.slot_shoes_desc),
    SlotInfo("bags", Res.drawable.ic_bag, Res.string.slot_bags, Res.string.slot_bags_desc),
    SlotInfo("accessories", Res.drawable.ic_glasses, Res.string.slot_accessories, Res.string.slot_accessories_desc)
)

private const val DEFAULT_LAYER_TEMP_C = 9   // базова температура, за якої радимо щільні шари
private val SignOutRed = Color(0xFFC0392B)

@Composable
fun ProfileScreen(
    settings: UserSettings,
    lightMode: Boolean,
    onLightModeChange: (Boolean) -> Unit,
    language: String,                       // "system" | "en" | "uk"
    onLanguageChange: (String) -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    var coldSensitivity by remember { mutableStateOf(settings.coldSensitivity) }
    val slotState = remember {
        mutableStateMapOf<String, Boolean>().apply {
            slots.forEach { put(it.key, settings.isSlotEnabled(it.key)) }
        }
    }

    // Якщо мова "system", підсвічуємо ту, що реально активна на пристрої
    val selectedLanguage = when (language) {
        "uk", "en" -> language
        else -> if (LocalAppLocale.current.startsWith("uk")) "uk" else "en"
    }

    // Сенсибільність 0..2 -> зсув температури (°C), як у бекенді: ColdSensitivity × 2
    val shift = (coldSensitivity * 2).roundToInt()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ---- Заголовок ----
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                stringResource(Res.string.profile_overline),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                stringResource(Res.string.profile_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        // ---- Користувач + місто ----
        SectionCard {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier.size(56.dp).clip(CircleShape).background(MaterialTheme.colorScheme.tertiary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("AK", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onTertiary)
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Anna Kovalenko", style = MaterialTheme.typography.titleMedium)
                        Pill(
                            stringResource(Res.string.pro_member),
                            MaterialTheme.colorScheme.secondaryContainer,
                            MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Text(
                        "anna.k@wearcast.app",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                stringResource(Res.string.weather_origin),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(start = 14.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_location),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(stringResource(Res.string.city_ternopil), style = MaterialTheme.typography.titleSmall)
                }
                TextButton(onClick = { AppLog.i("Change city pressed") }) {
                    Text(stringResource(Res.string.change))
                }
            }
        }

        // ---- Cold Sensitivity ----
        SectionCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TitleWithIcon(Res.drawable.ic_snowflake, stringResource(Res.string.cold_title), Modifier.weight(1f))
                Pill(
                    stringResource(Res.string.layer_bias, shift.toString()),
                    MaterialTheme.colorScheme.tertiaryContainer,
                    MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
            Text(
                stringResource(Res.string.cold_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Slider(
                value = coldSensitivity,
                onValueChange = { coldSensitivity = it },
                onValueChangeFinished = {
                    settings.coldSensitivity = coldSensitivity
                    AppLog.i("Cold sensitivity saved: $coldSensitivity")
                },
                valueRange = 0f..2f
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SliderLabel(stringResource(Res.string.cold_low))
                SliderLabel(stringResource(Res.string.cold_mid))
                SliderLabel(stringResource(Res.string.cold_high))
            }
            Text(
                stringResource(
                    Res.string.cold_hint,
                    (DEFAULT_LAYER_TEMP_C + shift).toString(),
                    DEFAULT_LAYER_TEMP_C.toString()
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(12.dp)
            )
        }

        // ---- Wardrobe Slots ----
        SectionCard {
            TitleWithIcon(Res.drawable.ic_wardrobe, stringResource(Res.string.slots_title))
            Text(
                stringResource(Res.string.slots_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            slots.forEach { slot ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(slot.icon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column(Modifier.weight(1f)) {
                        Text(stringResource(slot.title), style = MaterialTheme.typography.titleSmall)
                        Text(
                            stringResource(slot.description),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = slotState[slot.key] ?: true,
                        onCheckedChange = {
                            slotState[slot.key] = it
                            settings.setSlotEnabled(slot.key, it)
                            AppLog.i("Slot ${slot.key} enabled=$it")
                        }
                    )
                }
            }
        }

        // ---- Preferences ----
        SectionCard {
            TitleWithIcon(Res.drawable.ic_settings, stringResource(Res.string.preferences_title))
            Text(
                stringResource(Res.string.language_label),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            LanguageSwitch(selected = selectedLanguage, onSelect = onLanguageChange)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_sun),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(24.dp)
                )
                Column(Modifier.weight(1f)) {
                    Text(stringResource(Res.string.light_mode), style = MaterialTheme.typography.titleSmall)
                    Text(
                        stringResource(Res.string.light_mode_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(checked = lightMode, onCheckedChange = onLightModeChange)
            }
        }

        // ---- Sign out ----
        OutlinedButton(
            onClick = onSignOut,
            shape = CircleShape,
            border = BorderStroke(1.dp, SignOutRed.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(Res.string.sign_out), color = SignOutRed)
        }
        Text(
            stringResource(Res.string.app_version, "1.0.0", "1"),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )
    }
}

@Composable
private fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
    }
}

@Composable
private fun TitleWithIcon(icon: DrawableResource, text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun Pill(text: String, container: Color, content: Color) {
    Surface(shape = CircleShape, color = container) {
        Text(
            text,
            style = MaterialTheme.typography.labelSmall,
            color = content,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun SliderLabel(text: String) {
    Text(text, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

/** Перемикач мови English / Українська (рідні назви мов не перекладаються). */
@Composable
private fun LanguageSwitch(selected: String, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp)
    ) {
        listOf("en" to "English", "uk" to "Українська").forEach { (code, label) ->
            val isSelected = selected == code
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(CircleShape)
                    .background(if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent)
                    .clickable { onSelect(code) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (isSelected) "✓  $label" else label,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

package com.example.wear.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wear.core.AppLog
import com.example.wear.core.UserSettings
import com.example.wear.ui.profile.ProfileScreen
import com.example.wear.ui.today.TodayScreen
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import wear.shared.generated.resources.*

enum class MainTab(val label: StringResource, val icon: DrawableResource) {
    Today(Res.string.tab_today, Res.drawable.ic_today),
    Wardrobe(Res.string.tab_wardrobe, Res.drawable.ic_wardrobe),
    Outfits(Res.string.tab_outfits, Res.drawable.ic_outfits),
    Profile(Res.string.tab_profile, Res.drawable.ic_profile)
}

@Composable
fun MainScreen(
    settings: UserSettings,
    tab: MainTab,
    onTabChange: (MainTab) -> Unit,
    lightMode: Boolean,
    onLightModeChange: (Boolean) -> Unit,
    language: String,
    onLanguageChange: (String) -> Unit,
    onboardingCompleted: Boolean,
    onOnboardingCompletedChange: (Boolean) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.statusBars,
        bottomBar = {
            WearBottomBar(
                selected = tab,
                onSelect = {
                    onTabChange(it)
                    AppLog.d("Tab selected: ${it.name}")
                },
                onAdd = { AppLog.i("Add button pressed") }
            )
        }
    ) { inner ->
        val modifier = Modifier.padding(inner)
        when (tab) {
            MainTab.Today -> TodayScreen(
                onboardingCompleted = onboardingCompleted,
                onOnboardingCompletedChange = onOnboardingCompletedChange,
                modifier = modifier
            )
            MainTab.Wardrobe -> PlaceholderScreen(
                title = stringResource(Res.string.wardrobe_title),
                icon = Res.drawable.ic_wardrobe,
                text = stringResource(Res.string.wardrobe_placeholder),
                modifier = modifier
            )
            MainTab.Outfits -> PlaceholderScreen(
                title = stringResource(Res.string.outfits_title),
                icon = Res.drawable.ic_outfits,
                text = stringResource(Res.string.outfits_placeholder),
                modifier = modifier
            )
            MainTab.Profile -> ProfileScreen(
                settings = settings,
                lightMode = lightMode,
                onLightModeChange = onLightModeChange,
                language = language,
                onLanguageChange = onLanguageChange,
                onSignOut = {
                    AppLog.i("Signed out, onboarding will be shown again")
                    onOnboardingCompletedChange(false)
                },
                modifier = modifier
            )
        }
    }
}

/** Плаваюче нижнє меню: Today, Wardrobe, [+], Outfits, Profile. */
@Composable
private fun WearBottomBar(
    selected: MainTab,
    onSelect: (MainTab) -> Unit,
    onAdd: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 6.dp,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BarItem(MainTab.Today, selected == MainTab.Today) { onSelect(MainTab.Today) }
                BarItem(MainTab.Wardrobe, selected == MainTab.Wardrobe) { onSelect(MainTab.Wardrobe) }

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable(onClick = onAdd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                BarItem(MainTab.Outfits, selected == MainTab.Outfits) { onSelect(MainTab.Outfits) }
                BarItem(MainTab.Profile, selected == MainTab.Profile) { onSelect(MainTab.Profile) }
            }
        }
    }
}

@Composable
private fun BarItem(tab: MainTab, isSelected: Boolean, onClick: () -> Unit) {
    val color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    Column(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(tab.icon),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Text(
            stringResource(tab.label),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = color,
            maxLines = 1
        )
    }
}

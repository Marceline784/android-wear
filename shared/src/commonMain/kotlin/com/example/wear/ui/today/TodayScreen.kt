package com.example.wear.ui.today

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wear.core.AppLog
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import wear.shared.generated.resources.*

/** Тимчасовий екран Today (тема, Kermit, settings, локалізація). Справжній Today зробимо пізніше. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    onboardingCompleted: Boolean,
    onOnboardingCompletedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var selected by remember { mutableStateOf(0) }
    val filters = listOf(
        Res.string.filter_all, Res.string.filter_tops, Res.string.filter_bottoms,
        Res.string.filter_shoes, Res.string.filter_bags
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            stringResource(Res.string.app_name),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            stringResource(Res.string.today_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_sun),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(40.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        "12° / 4°",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(stringResource(Res.string.today_weather_details), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEachIndexed { index, res ->
                FilterChip(
                    selected = selected == index,
                    onClick = {
                        selected = index
                        AppLog.d("Filter selected: index=$index")
                    },
                    label = { Text(stringResource(res)) }
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    stringResource(Res.string.saved_settings_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    stringResource(
                        Res.string.onboarding_completed_label,
                        stringResource(if (onboardingCompleted) Res.string.common_yes else Res.string.common_no)
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(
                    onClick = { onOnboardingCompletedChange(!onboardingCompleted) },
                    shape = CircleShape,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        stringResource(if (onboardingCompleted) Res.string.reset_onboarding else Res.string.mark_onboarding)
                    )
                }
            }
        }

        Button(onClick = {}, shape = CircleShape, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(Res.string.choose_outfit))
        }
        OutlinedButton(onClick = {}, shape = CircleShape, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(Res.string.shuffle))
        }
    }
}

package com.example.wear.core.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow

/** Підписка екрана на одноразові ефекти ViewModel. */
@Composable
fun <E> ObserveEffects(effects: Flow<E>, onEffect: (E) -> Unit) {
    val latest = rememberUpdatedState(onEffect)
    LaunchedEffect(effects) {
        effects.collect { latest.value(it) }
    }
}

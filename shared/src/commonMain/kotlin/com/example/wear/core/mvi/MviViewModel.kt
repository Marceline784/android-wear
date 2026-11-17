package com.example.wear.core.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

/**
 * Базовий ViewModel для архітектури MVI.
 *
 *  - S (State)  : один незмінний об'єкт стану екрана, UI лише відображає його;
 *  - I (Intent) : дії користувача, які UI передає у [onIntent];
 *  - E (Effect) : одноразові події (навігація тощо), які не зберігаються в стані.
 */
abstract class MviViewModel<S : Any, I : Any, E : Any>(initialState: S) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effects = Channel<E>(Channel.BUFFERED)
    val effects: Flow<E> = _effects.receiveAsFlow()

    protected val currentState: S get() = _state.value

    abstract fun onIntent(intent: I)

    protected fun setState(reduce: S.() -> S) {
        _state.update { it.reduce() }
    }

    protected fun sendEffect(effect: E) {
        _effects.trySend(effect)
    }
}

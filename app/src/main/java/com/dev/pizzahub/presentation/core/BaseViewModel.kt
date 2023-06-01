package com.dev.pizzahub.presentation.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * BaseViewModel is an abstract base class for ViewModels with a specific state, intent and event type.
 *
 * @property S type of the state.
 * @property I type of the intent.
 * @property SE type of the single event.
 */
abstract class BaseViewModel<S : State, I : Intent, SE : SingleEvent> : ViewModel() {

    // Represents the current state of the UI.
    val viewState = MutableStateFlow(initState())

    // Represents a single event in the UI.
    var singleIntent = MutableSharedFlow<SE>(replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)

    /**
     * Process the intent and update the UI state accordingly.
     *
     * @param intent The intent to process.
     */
    fun sendIntent(intent: I) {
        viewModelScope.launch {
            viewState.emit(reduceIntent(intent, viewState.value))
            handleIntent(intent)
        }
    }

    /**
     * Trigger a single event.
     *
     * @param event The event to trigger.
     */
    fun triggerSingleEvent(event: SE) = viewModelScope.launch {
        singleIntent.tryEmit(event)
    }

    /**
     * Handle the intent.
     *
     * @param intent The intent to handle.
     */
    abstract suspend fun handleIntent(intent: I)

    /**
     * Reduce the intent and the current state to a new state.
     *
     * @param intent The intent to process.
     * @param state The current state.
     * @return The new state.
     */
    abstract fun reduceIntent(intent: I, state: S): S

    /**
     * Initialize the state.
     *
     * @return The initial state.
     */
    abstract fun initState(): S
}
package core.mvi

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Abstract base class for a ViewModel in an MVI architecture.
 *
 * @param Intent The type representing user actions or events.
 * @param State The type representing the state of the UI.
 * @param Effect The type representing one-time events from viewmodel to UI.
 * @param initialState The initial state of the UI.
 */
abstract class MviViewModel<Intent, State, Effect>(
    initialState: State,
) : ViewModel() {
    private val _state: MutableStateFlow<State> = MutableStateFlow(initialState)

    // Representing the state of the UI
    val state: StateFlow<State> = _state.asStateFlow()

    private val _effects: MutableStateFlow<ViewEffect<Effect?>> =
        MutableStateFlow(ViewEffect(null))

    // representing one-time events or messages to UI
    val effects: StateFlow<ViewEffect<Effect?>> = _effects.asStateFlow()

    /**
     * Abstract function to handle intents received from the UI.
     * Similar to reduce, processIntent, onUIEvent
     * @param intent The intent received from the UI.
     */
    abstract fun dispatch(intent: Intent)

    /**
     * Updates the current state using the provided lambda function.
     *
     * @param state A lambda that takes the current state and returns the new state.
     *
     * Note: Use a Mutex if calling this from multiple threads simultaneously.
     */
    protected fun updateState(state: State.() -> State) {
        _state.update(state)
    }

    /**
     * Pushes a new one-time event to be observed by the UI.
     *
     * @param effect The event to be pushed.
     */
    protected fun pushEffect(effect: Effect) {
        _effects.value = ViewEffect(effect)
    }

    @VisibleForTesting
    fun mockState(state: State.() -> State) {
        updateState(state)
    }
}
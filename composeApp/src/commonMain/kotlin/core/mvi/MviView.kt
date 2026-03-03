package core.mvi

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Base interface for a View in an MVI architecture.
 *
 * @param Intent The type representing user actions or events.
 * @param State The type representing the state of the view.
 * @param Effect The type representing one-time events or messages.
 * @param VM The type of the [MviViewModel] associated with this view.
 */
interface MviView<Intent, State, Effect, VM : MviViewModel<Intent, State, Effect>> {
    val viewModel: VM

    /**
     * Collects the ViewModel state and events to render the UI and handle one-time events.
     *
     * @param owner The LifecycleOwner which controls the observer.
     */
    fun collectViewModel(owner: LifecycleOwner) {
        owner.lifecycleScope.launch {
            // Collect the ViewModel's state and events when the lifecycle is in the STARTED state
            owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.state.collectLatest { renderState(it) } }
                launch {
                    viewModel.effects.collect {
                        it.getIfNotHandled()?.let { event ->
                            handleEffect(event)
                        }
                    }
                }
            }
        }
    }

    /**
     * Dispatches an intent to the ViewModel to handle user actions.
     *
     * @param intent The intent to be dispatched.
     */
    @CallSuper
    fun dispatch(intent: Intent) {
        viewModel.dispatch(intent)
    }

    /**
     * Handles one-time events from the ViewModel.
     *
     * @param effect The event to be handled.
     */
    fun handleEffect(effect: Effect)

    /**
     * Renders the state in the UI.
     *
     * @param state The state to be rendered.
     */
    fun renderState(state: State)
}
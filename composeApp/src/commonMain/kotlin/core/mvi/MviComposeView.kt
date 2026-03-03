package core.mvi

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

interface MviComposeView<Intent, State, Event, VM: MviViewModel<Intent, State, Event>>
    : MviView<Intent, State, Event, VM> {

    /**
     * Collects the ViewModel state and events to render the UI and handle one-time events.
     *
     * @param owner The LifecycleOwner which controls the observer.
     */
    override fun collectViewModel(owner: LifecycleOwner) {
        owner.lifecycleScope.launch {
            // Collect the ViewModel's state and events when the lifecycle is in the STARTED state
            owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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

    override fun renderState(state: State) {
        // Not necessarily need to be handled for composable at the moment due to the nature of compose MVI implementation
    }

}
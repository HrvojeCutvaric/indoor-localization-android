package co.be4you.indoorlocalization.viewmodel.main

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import co.be4you.indoorlocalization.navigation.Route

class MainViewModel : ViewModel() {

    val backStack = mutableStateListOf<Route>(Route.Registration)

    fun execute(action: MainAction) {
        when (action) {
            is MainAction.NavigateBack -> {
                backStack.removeLastOrNull()
            }

            is MainAction.NavigateTo -> {
                backStack.add(action.route)
            }
        }
    }
}

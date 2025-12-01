package co.be4you.indoorlocalization.viewmodel.main

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import co.be4you.indoorlocalization.navigation.Route

class MainViewModel : ViewModel() {

    val backStack = mutableStateListOf<Route>(Route.Registration)

    fun execute(action: MainAction) {
        when (action) {
            is MainAction.NavigateBack -> {
                action.route?.let {
                    val index = backStack.indexOf(it)
                    backStack.subList(index - 1, backStack.size).clear()
                } ?: backStack.removeLastOrNull()
            }

            is MainAction.NavigateTo -> {
                action.removeRoutes?.let {
                    backStack.removeAll(it)
                }
                backStack.add(action.route)
            }
        }
    }
}

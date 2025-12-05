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
                    if (index > 0) {
                        backStack.subList(index, backStack.size).clear()
                    }
                } ?: run {
                    if(backStack.size > 1){
                        backStack.removeLast()
                    }
                }
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

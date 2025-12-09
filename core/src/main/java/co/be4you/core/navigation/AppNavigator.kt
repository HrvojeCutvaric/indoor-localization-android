package co.be4you.core.navigation

import androidx.compose.runtime.mutableStateListOf

class AppNavigator {

    val backStack = mutableStateListOf<Route>(Route.Login)

    fun navigateTo(
        route: Route,
        removeRoutes: List<Route>? = null,
    ) {
        removeRoutes?.let {
            backStack.removeAll(it)
        }
        backStack.add(route)
    }

    fun navigateBack(
        route: Route? = null,
    ) {
        route?.let {
            val index = backStack.indexOf(it)
            backStack.subList(index - 1, backStack.size).clear()
        } ?: backStack.removeLastOrNull()
    }
}

package co.be4you.core.navigation

import androidx.compose.runtime.mutableStateListOf
import co.be4you.core.domain.storage.AppEncryptedSharedPreferences

class AppNavigator(
    appEncryptedSharedPreferences: AppEncryptedSharedPreferences,
) {

    val backStack = mutableStateListOf<Route>()

    init {
        val accessToken = appEncryptedSharedPreferences.getAccessToken()
        if (accessToken != null) navigateTo(Route.Dashboard)
        else navigateTo(Route.Login)
    }

    fun navigateTo(
        route: Route,
        removeRoutes: List<Route>? = null,
        clearBackStack: Boolean = false,
    ) {
        removeRoutes?.let {
            backStack.removeAll(it)
        }
        if (clearBackStack) backStack.clear()
        backStack.add(route)
    }

    fun navigateBack(
        route: Route? = null,
    ) {
        if (backStack.size <= 1) return
        route?.let {
            val index = backStack.indexOf(it)
            backStack.subList(index - 1, backStack.size).clear()
        } ?: backStack.removeLastOrNull()
    }

    inline fun <reified T : Route> getRouteOrNull(): T? =
        backStack.lastOrNull() as? T

}

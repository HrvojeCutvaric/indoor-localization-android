package co.be4you.indoorlocalization.viewmodel.main

import co.be4you.indoorlocalization.navigation.Route

sealed interface MainAction {

    data class NavigateTo(
        val route: Route,
        val removeRoutes: List<Route>? = null,
        val isInclusive: Boolean = false,
    ) : MainAction

    data class NavigateBack(
        val route: Route? = null,
        val isInclusive: Boolean = false,
    ) : MainAction
}

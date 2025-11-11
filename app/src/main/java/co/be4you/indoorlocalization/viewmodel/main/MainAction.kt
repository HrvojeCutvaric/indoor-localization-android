package co.be4you.indoorlocalization.viewmodel.main

import co.be4you.indoorlocalization.navigation.Route

sealed interface MainAction {

    data class NavigateTo(val route: Route) : MainAction

    data object NavigateBack : MainAction
}

package co.be4you.indoorlocalization.viewmodel.dashboard

import co.be4you.core.domain.models.FloorMap

sealed interface DashboardAction {

    data object OnDropdownExpandedChanged : DashboardAction

    data object OnDismissRequest : DashboardAction

    data object OnLogoutClicked : DashboardAction

    data class OnFloorMapSelected(val floorMap: FloorMap) : DashboardAction

    data class OnNavigateToAssets(
        val floorMapId: Long,
        val floorMapName: String
    ): DashboardAction
}

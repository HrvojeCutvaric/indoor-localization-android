package co.be4you.indoorlocalization.viewmodel.assetdetail

import co.be4you.indoorlocalization.viewmodel.dashboard.DashboardAction

sealed interface AssetDetailAction {

    data object OnBackClicked : AssetDetailAction
    data object OnLogoutClicked : AssetDetailAction

    data object OnDeleteClicked : AssetDetailAction
}

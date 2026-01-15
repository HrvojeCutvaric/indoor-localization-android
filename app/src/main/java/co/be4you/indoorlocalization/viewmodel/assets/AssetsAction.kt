package co.be4you.indoorlocalization.viewmodel.assets

sealed interface AssetsAction {

    data class OnSearchChanged(val query: String) : AssetsAction

    data class OnAssetClicked(val assetId: Long) : AssetsAction

    data object OnBackClicked : AssetsAction

    data object OnLogoutClicked : AssetsAction
}

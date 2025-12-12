package co.be4you.indoorlocalization.viewmodel.assetdetail

sealed interface AssetDetailAction {

    data object OnBackClicked : AssetDetailAction
}

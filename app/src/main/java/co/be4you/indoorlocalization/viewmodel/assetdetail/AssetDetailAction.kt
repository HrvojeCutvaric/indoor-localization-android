package co.be4you.indoorlocalization.viewmodel.assetdetail

sealed interface AssetDetailAction {
    data class Load(val id: Long): AssetDetailAction
}
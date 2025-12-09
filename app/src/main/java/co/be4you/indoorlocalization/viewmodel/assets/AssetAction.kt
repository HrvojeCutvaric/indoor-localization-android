package co.be4you.indoorlocalization.viewmodel.assets

sealed interface AssetAction {
    data class OnSearchChanged(val query:String): AssetAction
}
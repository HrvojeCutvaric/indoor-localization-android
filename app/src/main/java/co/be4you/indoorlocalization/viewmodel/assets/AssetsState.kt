package co.be4you.indoorlocalization.viewmodel.assets

data class AssetsState(
    val assets: List<AssetUi> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
)
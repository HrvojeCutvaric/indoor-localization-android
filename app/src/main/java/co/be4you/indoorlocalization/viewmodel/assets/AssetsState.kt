package co.be4you.indoorlocalization.viewmodel.assets

import co.be4you.core.domain.models.Asset

data class AssetsState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val assets: List<Asset> = emptyList(),
    val filteredAssets: List<Asset> = emptyList(),
    val errorMessage: String? = null
)

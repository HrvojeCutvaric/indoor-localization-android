package co.be4you.indoorlocalization.viewmodel.assetdetail

import co.be4you.core.domain.models.Asset

data class AssetDetailState(
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val asset: Asset? = null,
    val errorResource: Int? = null
)
package co.be4you.indoorlocalization.viewmodel.createasset

import co.be4you.core.domain.models.FloorMap

data class CreateEditAssetState(
    val floorMap: FloorMap,
    val isSaving: Boolean = false,
    val name: String = "",
    val colorHex: String = "",
    val errorResource: Int? = null
)

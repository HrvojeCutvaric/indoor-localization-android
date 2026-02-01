package co.be4you.indoorlocalization.viewmodel.createasset

import androidx.compose.ui.graphics.Color
import co.be4you.core.domain.models.Asset
import co.be4you.core.domain.models.FloorMap

data class CreateEditAssetState(
    val floorMap: FloorMap,
    val asset: Asset?,
    val isSaving: Boolean,
    val name: String,
    val colorHex: String,
    val errorResource: Int?,
    val showDeleteDialog: Boolean,
    val showColorPicker: Boolean,
    val selectedColor: Color,
)

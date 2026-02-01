package co.be4you.indoorlocalization.viewmodel.createasset

import androidx.compose.ui.graphics.Color

interface CreateEditAssetAction {

    data class OnNameChanged(val value: String) : CreateEditAssetAction

    data class OnColorChanged(val value: String) : CreateEditAssetAction

    data class OnColorPickerConfirmed(val color: Color) : CreateEditAssetAction

    data object OnConfirmClicked : CreateEditAssetAction

    data object OnBackClicked : CreateEditAssetAction

    data object OnDeleteClicked : CreateEditAssetAction

    data object OnConfirmDeleteClicked : CreateEditAssetAction

    data object OnDismissDeleteClicked : CreateEditAssetAction

    data object OnColorPickerClicked : CreateEditAssetAction

    data object OnColorPickerDismissed : CreateEditAssetAction
}

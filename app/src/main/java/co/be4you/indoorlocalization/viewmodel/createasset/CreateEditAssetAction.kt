package co.be4you.indoorlocalization.viewmodel.createasset

interface CreateEditAssetAction {

    data class OnNameChanged(val value: String) : CreateEditAssetAction

    data class OnColorChanged(val value: String) : CreateEditAssetAction

    data object OnConfirmClicked : CreateEditAssetAction

    data object OnBackClicked : CreateEditAssetAction

    data object OnDeleteClicked : CreateEditAssetAction

    data object OnConfirmDeleteClicked : CreateEditAssetAction

    data object OnDismissDeleteClicked : CreateEditAssetAction
}

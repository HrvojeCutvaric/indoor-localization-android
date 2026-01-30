package co.be4you.indoorlocalization.viewmodel.createasset

interface AddAssetAction {

    data class OnNameChanged(val value: String) : AddAssetAction

    data class OnXChanged(val value: String) : AddAssetAction

    data class OnYChanged(val value: String) : AddAssetAction

    data class OnColorChanged(val value: String) : AddAssetAction

    data class OnActiveChanged(val value: Boolean) : AddAssetAction

    data object OnSaveClicked : AddAssetAction

    data object OnBackClicked : AddAssetAction
}

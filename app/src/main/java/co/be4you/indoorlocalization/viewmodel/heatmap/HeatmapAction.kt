package co.be4you.indoorlocalization.viewmodel.heatmap

import co.be4you.core.domain.models.Asset

sealed interface HeatmapAction {

    data object OnSaveSelectedAssetsClicked : HeatmapAction

    data object OnGenerateClicked : HeatmapAction

    data object OnBackToDashboardClicked : HeatmapAction

    data object OnCloseDateTimePicker : HeatmapAction

    data object OnAddAssetClicked : HeatmapAction

    data object OnShowZonesClicked : HeatmapAction

    data object OnBackToFiltersClicked : HeatmapAction

    data class OnRemoveAssetClicked(val asset: Asset) : HeatmapAction

    data class OnDateSelected(val date: Long?) : HeatmapAction

    data class OnTimeConfirm(val hour: Int, val minute: Int) : HeatmapAction

    data class OnDateTimeFieldClicked(
        val option: HeatmapState.Companion.DateTimeOption
    ) : HeatmapAction

    data class OnAssetSelected(val asset: Asset) : HeatmapAction

    data class OnSearchChanged(val query: String) : HeatmapAction
}

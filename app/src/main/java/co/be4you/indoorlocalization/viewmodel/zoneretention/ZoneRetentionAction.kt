package co.be4you.indoorlocalization.viewmodel.zoneretention

import co.be4you.core.domain.models.Asset
import co.be4you.indoorlocalization.viewmodel.heatmap.HeatmapAction
import co.be4you.indoorlocalization.viewmodel.heatmap.HeatmapState

sealed interface ZoneRetentionAction{
    data object OnBackClicked: ZoneRetentionAction

    data object OnGenerateClicked: ZoneRetentionAction

    data object OnCloseDateTimePicker : ZoneRetentionAction

    data object OnAddAssetClicked : ZoneRetentionAction

    data class OnTimeConfirm(val hour: Int, val minute: Int) : ZoneRetentionAction

    data class OnDateTimeFieldClicked(
        val option: HeatmapState.Companion.DateTimeOption
    ) : ZoneRetentionAction

    data class OnAssetSelected(val asset: Asset) : ZoneRetentionAction

    data object OnAddZoneClicked : ZoneRetentionAction
}
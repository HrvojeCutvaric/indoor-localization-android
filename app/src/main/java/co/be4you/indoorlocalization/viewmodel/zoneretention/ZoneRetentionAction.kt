package co.be4you.indoorlocalization.viewmodel.zoneretention

import co.be4you.core.domain.models.Asset
import co.be4you.core.domain.models.Zone

sealed interface ZoneRetentionAction {

    data object OnBackClicked : ZoneRetentionAction

    data object OnGenerateClicked : ZoneRetentionAction

    data object OnCloseDateTimePicker : ZoneRetentionAction

    data object OnAssetDropdownExpandedChanged : ZoneRetentionAction
    data object OnZoneDropdownExpandedChanged : ZoneRetentionAction
    data object OnDismissAssetDropdown : ZoneRetentionAction
    data object OnDismissZoneDropdown : ZoneRetentionAction

    data class OnAssetSelected(val asset: Asset) : ZoneRetentionAction
    data class OnZoneSelected(val zone: Zone) : ZoneRetentionAction

    data class OnDateSelected(val date: Long?) : ZoneRetentionAction
    data class OnTimeConfirm(val hour: Int, val minute: Int) : ZoneRetentionAction
    data object OnBackToFiltersClicked : ZoneRetentionAction
    data class OnDateTimeFieldClicked(
        val option: ZoneRetentionState.Companion.DateTimeOption
    ) : ZoneRetentionAction
}
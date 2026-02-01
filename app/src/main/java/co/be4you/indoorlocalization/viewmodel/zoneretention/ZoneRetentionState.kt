package co.be4you.indoorlocalization.viewmodel.zoneretention

import co.be4you.core.domain.models.Asset
import co.be4you.core.domain.models.Zone
import co.be4you.core.domain.models.ZoneRetentionHistory
import co.be4you.indoorlocalization.utils.ZoneRetentionScreenMode

data class ZoneRetentionState(
    val mode: ZoneRetentionScreenMode = ZoneRetentionScreenMode.FILTERS,

    val assets: List<Asset> = emptyList(),
    val zones: List<Zone> = emptyList(),

    val selectedAsset: Asset? = null,
    val selectedZone: Zone? = null,

    val isAssetDropdownExpanded: Boolean = false,
    val isZoneDropdownExpanded: Boolean = false,

    val fromDateTime: Long? = null,
    val toDateTime: Long? = null,
    val activeDateTimeOption: DateTimeOption? = null,

    val isButtonLoading: Boolean = false,
    val errorRes: Int? = null,

    val reportRows: List<ZoneRetentionHistory> = emptyList(),

) {
    companion object {
        enum class DateTimeOption {
            FROM_DATE, FROM_TIME, TO_DATE, TO_TIME
        }
    }
}

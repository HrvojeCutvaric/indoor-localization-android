package co.be4you.indoorlocalization.viewmodel.heatmap

import co.be4you.core.domain.models.Asset
import co.be4you.core.domain.models.AssetPositionHistory
import co.be4you.core.domain.models.FloorMap
import co.be4you.core.domain.models.Zone
import co.be4you.indoorlocalization.utils.HeatmapScreenMode

data class HeatmapState(
    val mode: HeatmapScreenMode = HeatmapScreenMode.FILTERS,
    val floorMap: FloorMap? = null,
    val assets: List<Asset> = emptyList(),
    val selectedAssets: List<Asset> = emptyList(),
    val showZones: Boolean = false,
    val zones: List<Zone> = emptyList(),
    val fromDateTime: Long? = null,
    val toDateTime: Long? = null,
    val errorRes: Int? = null,
    val activeDateTimeOption: DateTimeOption? = null,
    val isButtonLoading: Boolean = false,
    val preselectedAssets: List<Asset> = emptyList(),
    val unselectedAssets: List<Asset> = emptyList(),
    val searchQuery: String,
    val assetPositionHistory: List<AssetPositionHistory> = emptyList(),
) {
    companion object {
        enum class DateTimeOption {
            FROM_DATE, FROM_TIME, TO_DATE, TO_TIME
        }
    }

    fun isSelected(asset: Asset) = preselectedAssets.contains(asset)
}

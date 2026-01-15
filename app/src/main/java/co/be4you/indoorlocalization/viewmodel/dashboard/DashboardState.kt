package co.be4you.indoorlocalization.viewmodel.dashboard

import co.be4you.core.domain.models.Asset
import co.be4you.core.domain.models.FloorMap
import co.be4you.core.domain.models.Zone

data class DashboardState(
    val floorMaps: List<FloorMap>,
    val selectedFloorMap: FloorMap?,
    val isDropdownExpanded: Boolean,
    val floorMapAssets: List<Asset>,
    val isButtonLoading: Boolean = false,
    val floorMapZones: List<Zone>,
)

package co.be4you.core.data.repositories

import co.be4you.core.data.network.services.AssetTrackingService
import co.be4you.core.domain.models.Asset
import kotlinx.coroutines.flow.Flow

class AssetTrackingRepository(
    private val assetTrackingService: AssetTrackingService,
) {

    fun assetsPosition(floorMapId: Long): Flow<List<Asset>> {
        return assetTrackingService.assetsPosition(floorMapId = floorMapId)
    }
}

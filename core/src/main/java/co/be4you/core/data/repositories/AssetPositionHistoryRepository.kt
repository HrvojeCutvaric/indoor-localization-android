package co.be4you.core.data.repositories

import co.be4you.core.data.network.services.AssetPositionHistoryService
import co.be4you.core.domain.models.AssetPositionHistory

class AssetPositionHistoryRepository(
    private val assetPositionHistoryService: AssetPositionHistoryService
) {

    suspend fun getAssetPositionHistory(
        floorMapId: Long,
        from: Long,
        to: Long,
    ): Result<List<AssetPositionHistory>> {
        return assetPositionHistoryService.getAssetPositionHistory(floorMapId, from, to)
    }
}

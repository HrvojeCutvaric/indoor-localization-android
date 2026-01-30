package co.be4you.core.data.network.services

import co.be4you.core.domain.models.AssetPositionHistory

interface AssetPositionHistoryService {

    suspend fun getAssetPositionHistory(
        floorMapId: Long,
        from: Long,
        to: Long,
    ): Result<List<AssetPositionHistory>>
}

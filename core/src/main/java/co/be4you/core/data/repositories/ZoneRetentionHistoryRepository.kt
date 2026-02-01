package co.be4you.core.data.repositories

import co.be4you.core.data.network.services.ZoneRetentionHistoryService
import co.be4you.core.domain.models.ZoneRetentionHistory

class ZoneRetentionHistoryRepository(
    private val zoneRetentionHistoryService: ZoneRetentionHistoryService
) {
    suspend fun getZoneRetentionHistory(
        assetId: Long,
        zoneId: Long,
        from: Long,
        to: Long,
    ): Result<List<ZoneRetentionHistory>>{
        return zoneRetentionHistoryService.getZoneRetentionHistory(assetId, zoneId, from, to)
    }
}
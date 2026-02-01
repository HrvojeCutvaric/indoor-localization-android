package co.be4you.core.data.network.services

import co.be4you.core.domain.models.ZoneRetentionHistory

interface ZoneRetentionHistoryService {
    suspend fun getZoneRetentionHistory (
        assetId: Long,
        zoneId: Long,
        from: Long,
        to: Long
    ): Result<List<ZoneRetentionHistory>>
}
package co.be4you.core.data.network.ws.api.mappers

import co.be4you.core.data.network.ws.api.models.zone_retention_history.ZoneRetentionHistoryDto
import co.be4you.core.domain.models.ZoneRetentionHistory

fun ZoneRetentionHistoryDto.toZoneRetentionHistory(): ZoneRetentionHistory =
    ZoneRetentionHistory(
        id = this.id,
        assetId = this.assetId,
        zoneId = this.zoneId,
        enterDateTime = this.enterDateTime.toMillis() ?: 0L,
        exitDateTime = this.exitDateTime?.toMillis() ?: 0L,
    )


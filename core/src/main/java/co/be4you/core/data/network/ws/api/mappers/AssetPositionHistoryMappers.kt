package co.be4you.core.data.network.ws.api.mappers

import co.be4you.core.data.network.ws.api.models.asset_position_history.AssetPositonHistoryDto
import co.be4you.core.domain.models.AssetPositionHistory
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun AssetPositonHistoryDto.toAssetPositionHistory(): AssetPositionHistory {
    val millis: Long? = runCatching {
        Instant.parse(this.dateTime).toEpochMilliseconds()
    }.getOrNull()

    return AssetPositionHistory(
        id = this.id,
        assetId = this.assetId,
        floorMapId = this.floorMapId,
        x = this.x,
        y = this.y,
        dateTime = millis ?: 0L,
    )
}

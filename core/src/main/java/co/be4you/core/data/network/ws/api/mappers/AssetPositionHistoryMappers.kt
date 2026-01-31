package co.be4you.core.data.network.ws.api.mappers

import co.be4you.core.data.network.ws.api.models.asset_position_history.AssetPositonHistoryDto
import co.be4you.core.domain.models.AssetPositionHistory

fun AssetPositonHistoryDto.toAssetPositionHistory(): AssetPositionHistory =
    AssetPositionHistory(
        id = this.id,
        assetId = this.assetId,
        floorMapId = this.floorMapId,
        x = this.x,
        y = this.y,
        dateTime = this.dateTime.toMillis() ?: 0L,
    )


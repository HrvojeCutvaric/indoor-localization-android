package co.be4you.core.data.network.ws.api.mappers

import co.be4you.core.data.network.ws.api.models.assets.AssetDto
import co.be4you.core.domain.models.Asset

fun AssetDto.toAsset(): Asset =
    Asset(
        id = this.id,
        name = this.name,
        colorHex = this.color,
        x = this.x,
        y = this.y,
        floorMapId = this.floorMapId,
        active = this.active,
        lastSync = this.lastSync?.toMillis(),
    )

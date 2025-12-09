package co.be4you.core.data.network.ws.api.mappers

import android.util.Log
import co.be4you.core.data.network.ws.api.models.assets.AssetDto
import co.be4you.core.domain.models.Asset
import java.time.Instant

fun AssetDto.toAsset(): Asset {

    return Asset(
        id = id,
        name = name,
        colorHex = color,
        x = x,
        y = y,
        floorMapId = floorMapId,
        active = active,
        lastSync = lastSync
            ?.takeIf { it.isNotBlank() }
            ?.let { Instant.parse(it)}
    )
}

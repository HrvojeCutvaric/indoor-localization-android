package co.be4you.core.data.network.ws.api.mappers

import co.be4you.core.BuildConfig
import co.be4you.core.data.network.ws.api.models.floorMap.FloorMapDto
import co.be4you.core.domain.models.FloorMap

fun FloorMapDto.toFloorMap(): FloorMap =
    FloorMap(
        id = id,
        name = name,
        imageUrl = "${BuildConfig.BASE_URL_BACKEND}${imageUrl.orEmpty()}",
        imageWidthPx = imageWidthPx ?: 0,
        imageHeightPx = imageHeightPx ?: 0,
        widthInMeters = widthInMeters,
        heightInMeters = heightInMeters,
    )

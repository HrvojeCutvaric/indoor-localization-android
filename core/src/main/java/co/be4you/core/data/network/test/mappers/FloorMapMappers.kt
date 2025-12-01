package co.be4you.core.data.network.test.mappers

import co.be4you.core.data.network.test.models.TestFloorMap
import co.be4you.core.domain.models.FloorMap

fun TestFloorMap.toFloorMap(): FloorMap =
    FloorMap(
        id = id,
        name = name,
        imageUrl = imageUrl,
    )

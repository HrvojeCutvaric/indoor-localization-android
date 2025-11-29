package co.be4you.indoorlocalization.data.apis.test.mappers

import co.be4you.indoorlocalization.data.apis.test.models.TestFloorMap
import co.be4you.indoorlocalization.domain.models.FloorMap

fun TestFloorMap.toFloorMap(): FloorMap =
    FloorMap(
        id = id,
        name = name,
        imageUrl = imageUrl,
    )

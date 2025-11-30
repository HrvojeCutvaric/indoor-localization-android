package co.be4you.indoorlocalization.data.repositories

import co.be4you.indoorlocalization.data.apis.FloorMapApi
import co.be4you.indoorlocalization.domain.models.FloorMap

class FloorMapRepository(
    private val floorMapApi: FloorMapApi,
) {

    suspend fun getFloorMap(id: Long): Result<FloorMap> = floorMapApi.getFloorMap(id = id)
}

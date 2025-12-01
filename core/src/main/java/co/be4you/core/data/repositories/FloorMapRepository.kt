package co.be4you.core.data.repositories

import co.be4you.core.data.network.FloorMapApi
import co.be4you.core.domain.models.FloorMap

class FloorMapRepository(
    private val floorMapApi: FloorMapApi,
) {

    suspend fun getFloorMap(id: Long): Result<FloorMap> = floorMapApi.getFloorMap(id = id)
}

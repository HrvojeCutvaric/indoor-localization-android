package co.be4you.core.data.repositories

import co.be4you.core.data.network.services.FloorMapService
import co.be4you.core.domain.models.FloorMap

class FloorMapRepository(
    private val floorMapService: FloorMapService,
) {

    suspend fun getFloorMap(id: Long): Result<FloorMap> = floorMapService.getFloorMap(id = id)
}

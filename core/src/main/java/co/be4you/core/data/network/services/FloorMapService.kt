package co.be4you.core.data.network.services

import co.be4you.core.domain.models.FloorMap

interface FloorMapService {

    suspend fun getFloorMap(id: Long): Result<FloorMap>

    suspend fun getFloorMaps(): Result<List<FloorMap>>
}

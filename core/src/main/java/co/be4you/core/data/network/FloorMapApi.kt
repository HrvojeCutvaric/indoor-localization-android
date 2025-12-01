package co.be4you.core.data.network

import co.be4you.core.domain.models.FloorMap

interface FloorMapApi {

    suspend fun getFloorMap(id: Long): Result<FloorMap>
}

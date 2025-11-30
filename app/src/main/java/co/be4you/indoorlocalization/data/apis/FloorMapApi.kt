package co.be4you.indoorlocalization.data.apis

import co.be4you.indoorlocalization.domain.models.FloorMap

interface FloorMapApi {

    suspend fun getFloorMap(id: Long): Result<FloorMap>
}

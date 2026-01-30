package co.be4you.core.data.network.ws

import co.be4you.core.data.network.services.FloorMapService
import co.be4you.core.data.network.ws.api.FloorMapApi
import co.be4you.core.data.network.ws.api.mappers.toFloorMap
import co.be4you.core.data.network.ws.api.utils.apiCallListMap
import co.be4you.core.data.network.ws.api.utils.apiCallMap
import co.be4you.core.domain.models.FloorMap

class WSFloorMapService(
    private val floorMapApi: FloorMapApi,
) : FloorMapService {

    override suspend fun getFloorMap(id: Long): Result<FloorMap> =
        apiCallMap(
            call = { floorMapApi.getFloorMap(id = id) },
            errorMessage = "Failed to fetch floor map",
            mapper = { it.toFloorMap() }
        )

    override suspend fun getFloorMaps(): Result<List<FloorMap>> =
        apiCallListMap(
            call = { floorMapApi.getFloorMaps() },
            errorMessage = "Failed to fetch floor maps",
            mapper = { it.toFloorMap() }
        )
}

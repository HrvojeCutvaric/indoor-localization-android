package co.be4you.core.data.network.ws.api

import co.be4you.core.data.network.ws.api.models.utils.ApiResponse
import co.be4you.core.data.network.ws.api.models.floorMap.FloorMapDto
import retrofit2.http.GET
import retrofit2.http.Path

interface FloorMapApi {

    @GET("/api/FloorMaps")
    suspend fun getFloorMaps(): ApiResponse<List<FloorMapDto>>

    @GET("/api/FloorMaps/{id}")
    suspend fun getFloorMap(
        @Path("id") id: Long,
    ): ApiResponse<FloorMapDto>
}

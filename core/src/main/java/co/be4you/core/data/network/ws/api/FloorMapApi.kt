package co.be4you.core.data.network.ws.api

import co.be4you.core.data.network.ws.api.models.floorMap.FloorMapDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FloorMapApi {

    @GET("/api/FloorMaps")
    suspend fun getFloorMaps(): Response<List<FloorMapDto>>

    @GET("/api/FloorMaps/{id}")
    suspend fun getFloorMap(
        @Path("id") id: Long,
    ): Response<FloorMapDto>
}

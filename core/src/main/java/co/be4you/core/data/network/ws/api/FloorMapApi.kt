package co.be4you.core.data.network.ws.api

import co.be4you.core.data.network.ws.api.models.FloorMapDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface FloorMapApi {

    @GET("/api/FloorMaps")
    suspend fun getFloorMaps(
        @Header("Authorization") token: String,
    ): Response<List<FloorMapDto>>

    @GET("/api/FloorMaps/{id}")
    suspend fun getFloorMap(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<FloorMapDto>
}

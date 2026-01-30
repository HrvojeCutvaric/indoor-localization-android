package co.be4you.core.data.network.ws.api

import co.be4you.core.data.network.ws.api.models.utils.ApiResponse
import co.be4you.core.data.network.ws.api.models.zones.ZoneDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ZoneApi {

    @GET("api/Zone/floormap/{floorMapId}")
    suspend fun getZones(
        @Path("floorMapId") floorMapId: Long,
    ): ApiResponse<List<ZoneDto>>
}

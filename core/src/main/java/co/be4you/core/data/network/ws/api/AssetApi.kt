package co.be4you.core.data.network.ws.api

import co.be4you.core.data.network.ws.api.models.assets.AssetDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AssetApi {

    @GET("api/Asset/floormap/{floorMapId}")
    suspend fun getAssetsByFloor(
        @Path("floorMapId") floorMapId: Long
    ): Response<List<AssetDto>>
}
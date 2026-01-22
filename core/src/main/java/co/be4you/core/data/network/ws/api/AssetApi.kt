package co.be4you.core.data.network.ws.api

import co.be4you.core.data.network.ws.api.models.assets.AssetDto
import co.be4you.core.data.network.ws.api.models.assets.CreateAssetRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AssetApi {

    @GET("api/Asset/floormap/{floorMapId}")
    suspend fun getAssetsByFloor(
        @Path("floorMapId") floorMapId: Long
    ): Response<List<AssetDto>>

    @GET("api/Asset/{id}")
    suspend fun getAssetById(
        @Path("id") id: Long
    ): Response<AssetDto>

    @DELETE("api/Asset/{id}")
    suspend fun deleteAsset(
        @Path("id") id: Long
    ): Response<Unit>

    @POST("/api/Asset")
    suspend fun createAsset(
        @Body request: CreateAssetRequestDto
    ): Response<Unit>
}
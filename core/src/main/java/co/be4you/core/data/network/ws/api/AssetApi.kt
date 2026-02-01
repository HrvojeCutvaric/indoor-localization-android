package co.be4you.core.data.network.ws.api

import co.be4you.core.data.network.ws.api.models.assets.AssetDto
import co.be4you.core.data.network.ws.api.models.assets.CreateAssetRequestDto
import co.be4you.core.data.network.ws.api.models.assets.EditAssetRequestDto
import co.be4you.core.data.network.ws.api.models.utils.ApiResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AssetApi {

    @GET("api/Asset/floormap/{floorMapId}")
    suspend fun getAssetsByFloor(
        @Path("floorMapId") floorMapId: Long
    ): ApiResponse<List<AssetDto>>

    @GET("api/Asset/{id}")
    suspend fun getAssetById(
        @Path("id") id: Long
    ): ApiResponse<AssetDto>

    @DELETE("api/Asset/{id}")
    suspend fun deleteAsset(
        @Path("id") id: Long
    ): ApiResponse<Unit>

    @POST("/api/Asset")
    suspend fun createAsset(
        @Body body: CreateAssetRequestDto
    ): ApiResponse<Unit>

    @PUT("/api/Asset/{id}")
    suspend fun updateAsset(
        @Path("id") id: Long,
        @Body body: EditAssetRequestDto
    ): ApiResponse<Unit>
}

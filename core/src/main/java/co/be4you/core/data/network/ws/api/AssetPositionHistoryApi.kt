package co.be4you.core.data.network.ws.api

import co.be4you.core.data.network.ws.api.models.asset_position_history.AssetPositonHistoryDto
import co.be4you.core.data.network.ws.api.models.utils.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AssetPositionHistoryApi {

    @GET("api/Reports/floormaps/{floorMapId}/heatmap")
    suspend fun getAssetPositionHistory(
        @Path("floorMapId") floorMapId: Long,
        @Query("from") from: String?,
        @Query("to") to: String?,
    ): ApiResponse<List<AssetPositonHistoryDto>>
}

package co.be4you.core.data.network.ws.api

import co.be4you.core.data.network.ws.api.models.utils.ApiResponse
import co.be4you.core.data.network.ws.api.models.zone_retention_history.ZoneRetentionHistoryDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ZoneRetentionHistoryApi {

    @GET("api/Reports/zones/retention")
    suspend fun getZoneRetentionHistory(
        @Query("assetId") assetId: Long,
        @Query("zoneId") zoneId: Long,
        @Query("from") from: String,
        @Query("to") to: String
        ): ApiResponse<List<ZoneRetentionHistoryDto>>
}

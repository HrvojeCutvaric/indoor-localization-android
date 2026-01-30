package co.be4you.core.data.network.ws

import co.be4you.core.data.network.services.AssetPositionHistoryService
import co.be4you.core.data.network.ws.api.AssetPositionHistoryApi
import co.be4you.core.data.network.ws.api.mappers.toAssetPositionHistory
import co.be4you.core.domain.models.AssetPositionHistory
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


@OptIn(ExperimentalTime::class)
class WSAssetPositionHistoryService(
    private val assetPositionHistoryApi: AssetPositionHistoryApi
) : AssetPositionHistoryService {

    override suspend fun getAssetPositionHistory(
        floorMapId: Long,
        from: Long,
        to: Long,
    ): Result<List<AssetPositionHistory>> {
        return try {
            val fromString = runCatching {
                Instant.fromEpochMilliseconds(from).toString()
            }.getOrNull()
            val toString = runCatching {
                Instant.fromEpochMilliseconds(to).toString()
            }.getOrNull()

            val response = assetPositionHistoryApi.getAssetPositionHistory(
                floorMapId = floorMapId,
                from = fromString,
                to = toString
            )

            if (response.isSuccessful) {
                val body = response.body() ?: return Result.failure((Throwable("Body is null")))
                Result.success(body.map { it.toAssetPositionHistory() })
            } else {
                Result.failure(Throwable("Failed to fetch asset position history"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

package co.be4you.core.data.network.ws

import co.be4you.core.data.network.services.ZoneRetentionHistoryService
import co.be4you.core.data.network.ws.api.ZoneRetentionHistoryApi
import co.be4you.core.data.network.ws.api.mappers.toZoneRetentionHistory
import co.be4you.core.data.network.ws.api.utils.apiCallListMap
import co.be4you.core.domain.models.ZoneRetentionHistory
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class WSZoneRetentionHistoryService(
    private val zoneRetentionHistoryApi: ZoneRetentionHistoryApi
): ZoneRetentionHistoryService {
    override suspend fun getZoneRetentionHistory(
        assetId: Long,
        zoneId: Long,
        from: Long,
        to: Long
    ): Result<List<ZoneRetentionHistory>> {

        val fromString = runCatching {
            Instant.fromEpochMilliseconds(from).toString()
        }.getOrElse {
            return Result.failure(Throwable("Invalid from timestamp"))
        }

        val toString = runCatching {
            Instant.fromEpochMilliseconds(to).toString()
        }.getOrElse {
            return Result.failure(Throwable("Invalid to timestamp"))
        }

        return apiCallListMap(
            call = {
                zoneRetentionHistoryApi.getZoneRetentionHistory(
                    assetId = assetId,
                    zoneId = zoneId,
                    from = fromString,
                    to = toString,
                )
            },
            errorMessage = "Failed to fetch zone retention history",
            mapper = { it.toZoneRetentionHistory() }
        )
    }
}
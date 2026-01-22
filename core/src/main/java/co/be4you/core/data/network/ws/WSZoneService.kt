package co.be4you.core.data.network.ws

import android.util.Log
import co.be4you.core.data.network.services.ZoneService
import co.be4you.core.data.network.ws.api.ZoneApi
import co.be4you.core.data.network.ws.api.mappers.toZone
import co.be4you.core.domain.models.Zone
import com.google.gson.Gson

class WSZoneService(
    private val zoneApi: ZoneApi,
    private val gson: Gson,
) : ZoneService {
    override suspend fun getZones(floorMapId: Long): Result<List<Zone>> {
        return try {
            val result = zoneApi.getZones(floorMapId = floorMapId)

            when (result.isSuccessful) {
                true -> {
                    val body = result.body() ?: return Result.failure(Throwable("Body is null"))

                    return Result.success(body.map { it.toZone(gson = gson) })
                }

                false -> {
                    return Result.failure(Throwable(message = "Failed to fetch zones"))
                }
            }
        } catch (error: Throwable) {
            Log.e("WSZoneService", "getZones: ", error)
            Result.failure(error)
        }
    }
}

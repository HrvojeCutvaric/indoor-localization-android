package co.be4you.core.data.network.ws

import co.be4you.core.data.network.services.ZoneService
import co.be4you.core.data.network.ws.api.ZoneApi
import co.be4you.core.data.network.ws.api.mappers.toZone
import co.be4you.core.data.network.ws.api.utils.apiCallListMap
import co.be4you.core.domain.models.Zone
import com.google.gson.Gson

class WSZoneService(
    private val zoneApi: ZoneApi,
    private val gson: Gson,
) : ZoneService {

    override suspend fun getZones(floorMapId: Long): Result<List<Zone>> =
        apiCallListMap(
            call = { zoneApi.getZones(floorMapId = floorMapId) },
            errorMessage = "Failed to fetch zones",
            mapper = { it.toZone(gson = gson) }
        )
}

package co.be4you.core.data.repositories

import co.be4you.core.data.network.services.ZoneService
import co.be4you.core.domain.models.Zone

class ZoneRepository(
    private val zoneService: ZoneService,
) {

    suspend fun getZones(floorMapId: Long): Result<List<Zone>> =
        zoneService.getZones(floorMapId = floorMapId)
}

package co.be4you.core.data.network.services

import co.be4you.core.domain.models.Zone

interface ZoneService {

    suspend fun getZones(floorMapId: Long): Result<List<Zone>>
}

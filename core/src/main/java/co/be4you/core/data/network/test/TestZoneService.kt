package co.be4you.core.data.network.test

import co.be4you.core.data.network.services.ZoneService
import co.be4you.core.domain.models.Zone
import co.be4you.core.domain.models.ZonePoint
import kotlinx.coroutines.delay

class TestZoneService : ZoneService {

    companion object {
        val mockZones = listOf(
            Zone(
                id = 1L,
                floorMapId = 1L,
                name = "Outbound",
                points = listOf(
                    ZonePoint(x = 600.0, y = 366.0, ordinalNumber = 1),
                    ZonePoint(x = 730.0, y = 366.0, ordinalNumber = 2),
                    ZonePoint(x = 730.0, y = 466.0, ordinalNumber = 3),
                    ZonePoint(x = 600.0, y = 466.0, ordinalNumber = 4)
                )
            ),
            Zone(
                id = 2L,
                floorMapId = 1L,
                name = "Racking 1",
                points = listOf(
                    ZonePoint(x = 440.0, y = 290.0, ordinalNumber = 1),
                    ZonePoint(x = 590.0, y = 290.0, ordinalNumber = 2),
                    ZonePoint(x = 590.0, y = 400.0, ordinalNumber = 3),
                    ZonePoint(x = 440.0, y = 400.0, ordinalNumber = 4)
                )
            ),
            Zone(
                id = 3L,
                floorMapId = 1L,
                name = "Racking 2",
                points = listOf(
                    ZonePoint(x = 10.0, y = 10.0, ordinalNumber = 1),
                    ZonePoint(x = 240.0, y = 10.0, ordinalNumber = 2),
                    ZonePoint(x = 240.0, y = 200.0, ordinalNumber = 3),
                    ZonePoint(x = 10.0, y = 200.0, ordinalNumber = 4)
                )
            )
        )
    }

    override suspend fun getZones(floorMapId: Long): Result<List<Zone>> {
        delay(500)
        val zones = mockZones.filter { it.floorMapId == floorMapId }
        return Result.success(zones)
    }
}

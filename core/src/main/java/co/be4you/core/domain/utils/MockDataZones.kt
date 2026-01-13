package co.be4you.core.domain.utils

import co.be4you.core.domain.models.Zone
import co.be4you.core.domain.models.ZonePoint

val mockZones = listOf(
    Zone(
        id = 1L,
        floorMapId = 1L,
        name = "Entrance Area",
        points = listOf(
            ZonePoint(x = 50.0, y = 550.0, ordinalNumber = 1),
            ZonePoint(x = 300.0, y = 550.0, ordinalNumber = 2),
            ZonePoint(x = 300.0, y = 650.0, ordinalNumber = 3),
            ZonePoint(x = 50.0, y = 650.0, ordinalNumber = 4)
        )
    ),
    Zone(
        id = 2L,
        floorMapId = 1L,
        name = "Main Hall",
        points = listOf(
            ZonePoint(x = 200.0, y = 150.0, ordinalNumber = 1),
            ZonePoint(x = 550.0, y = 150.0, ordinalNumber = 2),
            ZonePoint(x = 600.0, y = 400.0, ordinalNumber = 3),
            ZonePoint(x = 250.0, y = 420.0, ordinalNumber = 4)
        )
    ),
    Zone(
        id = 3L,
        floorMapId = 1L,
        name = "Office Zone",
        points = listOf(
            ZonePoint(x = 50.0, y = 50.0, ordinalNumber = 1),
            ZonePoint(x = 250.0, y = 50.0, ordinalNumber = 2),
            ZonePoint(x = 250.0, y = 200.0, ordinalNumber = 3),
            ZonePoint(x = 50.0, y = 200.0, ordinalNumber = 4)
        )
    )
)

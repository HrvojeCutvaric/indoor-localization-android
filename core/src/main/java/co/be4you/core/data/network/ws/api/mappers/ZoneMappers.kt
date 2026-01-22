package co.be4you.core.data.network.ws.api.mappers

import co.be4you.core.data.network.ws.api.models.zones.ZoneDto
import co.be4you.core.domain.models.Zone
import co.be4you.core.domain.models.ZonePoint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private fun parsePoints(pointsRaw: String, gson: Gson): List<ZonePoint> {
    val listType = object : TypeToken<List<ZonePoint>>() {}.type
    return gson.fromJson(pointsRaw, listType)
}

fun ZoneDto.toZone(gson: Gson): Zone = Zone(
    id = this.id,
    name = this.name,
    points = parsePoints(pointsRaw = this.points, gson = gson),
    floorMapId = this.floorMapId
)

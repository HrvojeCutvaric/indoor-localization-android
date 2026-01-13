package co.be4you.core.domain.models

data class Zone(
    val id: Long,
    val name: String,
    val points: List<ZonePoint>,
    val floorMapId: Long,
)


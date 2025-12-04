package co.be4you.core.domain.models

data class FloorMap(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val imageWidthPx: Int,
    val imageHeightPx: Int,
    val widthInMeters: Int,
    val heightInMeters: Int
)

package co.be4you.core.domain.models

data class AssetPositionHistory(
    val id: Long,
    val assetId: Long,
    val floorMapId: Long,
    val x: Double,
    val y: Double,
    val dateTime: Long,
)

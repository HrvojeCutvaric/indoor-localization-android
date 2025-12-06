package co.be4you.core.data.network.ws.api.models.assets

data class AssetDto (
    val id: Long,
    val name: String,
    val color: String?,
    val x: Double?,
    val y: Double?,
    val floorMapId: Long,
    val active: Boolean,
    val lastSync: String?,
)
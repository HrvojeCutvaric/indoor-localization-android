package co.be4you.core.data.network.ws.api.models.assets

data class CreateAssetRequestDto(
    val name: String,
    val x: Double?,
    val y: Double?,
    val floorMapId: Long,
    val active: Boolean,
    val color: String?
)
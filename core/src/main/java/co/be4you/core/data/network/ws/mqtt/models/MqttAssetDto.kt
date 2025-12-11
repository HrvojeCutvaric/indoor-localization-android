package co.be4you.core.data.network.ws.mqtt.models

data class MqttAssetDto(
    val id: Long,
    val name: String,
    val colorHex: String,
    val x: Double,
    val y: Double,
    val floorMapId: Long,
    val active: Boolean,
    val lastSync: String
)

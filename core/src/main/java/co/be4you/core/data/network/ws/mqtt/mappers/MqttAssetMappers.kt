package co.be4you.core.data.network.ws.mqtt.mappers

import co.be4you.core.data.network.ws.api.mappers.toMillis
import co.be4you.core.data.network.ws.mqtt.models.MqttAssetDto
import co.be4you.core.domain.models.Asset

fun MqttAssetDto.toAsset(): Asset =
    Asset(
        id = this.id,
        name = this.name,
        colorHex = this.colorHex,
        x = this.x,
        y = this.y,
        floorMapId = this.floorMapId,
        active = this.active,
        lastSync = this.lastSync.toMillis(),
    )

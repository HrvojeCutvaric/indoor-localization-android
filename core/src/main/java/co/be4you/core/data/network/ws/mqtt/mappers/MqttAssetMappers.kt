package co.be4you.core.data.network.ws.mqtt.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import co.be4you.core.data.network.ws.mqtt.models.MqttAssetDto
import co.be4you.core.domain.models.Asset
import java.time.Instant
import kotlin.time.ExperimentalTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalTime::class)
fun MqttAssetDto.toAsset(): Asset {

    val lastSync = Instant.parse(lastSync)

    return Asset(
        id = id,
        name = name,
        colorHex = colorHex,
        x = x,
        y = y,
        floorMapId = floorMapId,
        active = active,
        lastSync = lastSync,
    )
}

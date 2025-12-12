package co.be4you.core.data.network.ws.mqtt

import android.os.Build
import androidx.annotation.RequiresApi
import co.be4you.core.data.network.services.AssetTrackingService
import co.be4you.core.data.network.ws.mqtt.mappers.toAsset
import co.be4you.core.data.network.ws.mqtt.models.MqttAssetDto
import co.be4you.core.domain.models.Asset
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttMessageListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MqttAssetTrackingService(
    private val gson: Gson,
) : AssetTrackingService {

    companion object {
        private const val MQTT_SERVER_URL = "tcp://broker.hivemq.com:1883"
        private const val TOPIC = "air/assets/updates"
        private const val QOS = 1
    }

    private val client = MqttAsyncClient(
        MQTT_SERVER_URL,
        MqttClient.generateClientId(),
        MemoryPersistence()
    )

    private val incomingAssets = MutableSharedFlow<Asset>(extraBufferCapacity = 64)

    init {
        connectAndSubscribe()
    }

    private fun connectAndSubscribe() {
        val options = MqttConnectOptions().apply {
            isAutomaticReconnect = true
            isCleanSession = true
        }

        client.setCallback(null)

        client.connect(options, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                client.subscribe(TOPIC, QOS, object : IMqttMessageListener {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun messageArrived(topic: String?, message: MqttMessage?) {
                        try {
                            val json = message?.payload?.decodeToString() ?: return
                            val dto = gson.fromJson<MqttAssetDto>(
                                json,
                                object : TypeToken<MqttAssetDto>() {}.type
                            )
                            val asset = dto.toAsset()
                            incomingAssets.tryEmit(asset)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                })
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                exception?.printStackTrace()
            }
        })
    }

    override fun assetPosition(floorMapId: Long): Flow<Asset> =
        incomingAssets.filter { it.floorMapId == floorMapId }
}

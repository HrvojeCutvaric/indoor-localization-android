package co.be4you.core.data.network.ws.mqtt

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.be4you.core.data.network.services.AssetTrackingService
import co.be4you.core.data.network.ws.mqtt.mappers.toAsset
import co.be4you.core.data.network.ws.mqtt.models.MqttAssetDto
import co.be4you.core.domain.models.Asset
import co.be4you.core.domain.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttMessageListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MqttAssetTrackingService(
    private val gson: Gson,
) : AssetTrackingService {

    companion object {
        private fun assetListType() =
            object : TypeToken<Asset>() {}.type
    }

    private val persistence = MemoryPersistence()

    private val client: MqttAsyncClient =
        MqttAsyncClient(Constants.MQTT_SERVER_URL, MqttClient.generateClientId(), persistence)

    override fun assetPosition(floorMapId: Long): Flow<Asset> = callbackFlow {
        val topic = "air/assets/updates"

        val callback = object : MqttCallback {
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                try {
                    val json = message?.payload?.toString(Charsets.UTF_8) ?: return
                    val assets: Asset = gson.fromJson(json, assetListType())
                    trySend(assets)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun connectionLost(cause: Throwable?) {
                close(cause)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.v("MQTT", "Delivery complete")
            }
        }

        client.setCallback(callback)

        suspendCancellableCoroutine { cont ->
            val options = MqttConnectOptions().apply {
                isAutomaticReconnect = true
                isCleanSession = true
            }

            client.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    cont.resume(Unit)
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    cont.resumeWithException(exception ?: Exception("Unknown MQTT connect error"))
                }
            })
        }

        client.subscribe(topic, 1, object : IMqttMessageListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                try {
                    val json = message?.payload?.toString(Charsets.UTF_8) ?: return
                    val mqttAssetType = object : TypeToken<MqttAssetDto>() {}.type
                    val dto: MqttAssetDto = gson.fromJson(json, mqttAssetType)
                    val asset: Asset = dto.toAsset()
                    trySend(asset)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        awaitClose {
            client.unsubscribe(topic)
            client.setCallback(null)
        }
    }
}

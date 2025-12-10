package co.be4you.core.data.network.ws.mqtt

import co.be4you.core.data.network.services.AssetTrackingService
import co.be4you.core.domain.models.Asset
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockMqttAssetTrackingService : AssetTrackingService {

    companion object {
        private val mockAssets = listOf(
            Asset(
                id = 1,
                name = "forklift",
                colorHex = "#FF5733",
                x = 0.0,
                y = 0.0,
                floorMapId = 2,
                active = true,
                lastSync = null,
            ),
            Asset(
                id = 2,
                name = "forklift 1",
                colorHex = "#33A1FF",
                x = 1.0,
                y = 2.0,
                floorMapId = 2,
                active = true,
                lastSync = null,
            ),
        )
    }

    override fun assetsPosition(
        floorMapId: Long,
    ): Flow<List<Asset>> = flow {

        var current = mockAssets.filter { it.floorMapId == floorMapId }

        while (true) {
            emit(current)

            delay(1000L)

            current = current.mapIndexed { index, asset ->
                asset.copy(
                    x = (asset.x ?: 0.0) + 1,
                    y = (asset.y ?: 0.0) + 1,
                    lastSync = null,
                )
            }
        }
    }
}

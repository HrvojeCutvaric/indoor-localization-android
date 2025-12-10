package co.be4you.core.data.network.services

import co.be4you.core.domain.models.Asset
import kotlinx.coroutines.flow.Flow

interface AssetTrackingService {

    fun assetsPosition(floorMapId: Long): Flow<List<Asset>>
}

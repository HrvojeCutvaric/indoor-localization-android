package co.be4you.core.data.network.services

import co.be4you.core.domain.models.Asset
import co.be4you.core.domain.models.FloorMap

interface AssetService {
    suspend fun getAssetsByFloorMap(floorMapId: Long): Result<List<Asset>>
}
package co.be4you.core.data.network.services

import co.be4you.core.data.network.ws.api.models.assets.CreateAssetRequestDto
import co.be4you.core.domain.models.Asset
import co.be4you.core.domain.models.FloorMap

interface AssetService {
    suspend fun getAssetsByFloorMap(floorMapId: Long): Result<List<Asset>>

    suspend fun getAsset(id: Long): Result<Asset>

    suspend fun deleteAsset(id: Long): Result<Unit>

    suspend fun createAsset(request: CreateAssetRequestDto): Result<Unit>
}
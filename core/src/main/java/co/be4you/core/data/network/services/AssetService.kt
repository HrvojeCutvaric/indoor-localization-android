package co.be4you.core.data.network.services

import co.be4you.core.domain.models.Asset

interface AssetService {
    suspend fun getAssetsByFloorMap(floorMapId: Long): Result<List<Asset>>

    suspend fun getAsset(id: Long): Result<Asset>

    suspend fun deleteAsset(id: Long): Result<Unit>

    suspend fun createAsset(asset: Asset): Result<Unit>

    suspend fun updateAsset(asset: Asset): Result<Unit>
}

package co.be4you.core.data.repositories
import co.be4you.core.data.network.services.AssetService
import co.be4you.core.data.network.ws.api.models.assets.CreateAssetRequestDto
import co.be4you.core.domain.models.Asset

class AssetRepository(
    private val assetService: AssetService
){
    suspend fun getAssetsByFloorMap(floorMapId: Long): Result<List<Asset>>{
        return assetService.getAssetsByFloorMap(floorMapId)
    }

    suspend fun getAsset(id: Long): Result<Asset> {
        return assetService.getAsset(id)
    }

    suspend fun deleteAsset(id: Long): Result<Unit> {
        return assetService.deleteAsset(id)
    }

    suspend fun createAsset(request: CreateAssetRequestDto): Result<Unit>{
        return assetService.createAsset(request)
    }

}
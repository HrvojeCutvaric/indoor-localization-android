package co.be4you.core.data.repositories
import co.be4you.core.data.network.services.AssetService
import co.be4you.core.domain.models.Asset
import co.be4you.core.domain.models.FloorMap

class AssetRepository(
    private val assetService: AssetService
){
    suspend fun getAssetsByFloorMap(floorMapId: Long): Result<List<Asset>>{
        return assetService.getAssetsByFloorMap(floorMapId)
    }

    suspend fun getAsset(id: Long): Result<Asset> {
        return assetService.getAsset(id)
    }

}
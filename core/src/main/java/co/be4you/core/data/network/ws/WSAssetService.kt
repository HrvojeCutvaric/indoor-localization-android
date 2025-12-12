package co.be4you.core.data.network.ws

import co.be4you.core.data.network.services.AssetService
import co.be4you.core.data.network.ws.api.AssetApi
import co.be4you.core.data.network.ws.api.mappers.toAsset
import co.be4you.core.domain.models.Asset

class WSAssetService (
    private val assetApi: AssetApi
): AssetService{
    override suspend fun getAssetsByFloorMap(floorMapId: Long): Result<List<Asset>> {
        return try{
            val response = assetApi.getAssetsByFloor(floorMapId)

            if(response.isSuccessful){
                val body = response.body() ?: return Result.failure((Throwable("Body is null")))
                Result.success(body.map{it.toAsset()})
            }else{
                Result.failure(Throwable("Failed to fetch assets"))
            }
        }catch(e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun getAsset(id: Long): Result<Asset>{
        return try{

            val response = assetApi.getAssetById(id)

            if(response.isSuccessful){
                val body = response.body() ?: return Result.failure(Throwable("Body is null"))
                Result.success(body.toAsset())
            } else {
                Result.failure(Throwable("Failed to fetch asset"))
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}
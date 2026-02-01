package co.be4you.core.data.network.ws

import co.be4you.core.data.network.services.AssetService
import co.be4you.core.data.network.ws.api.AssetApi
import co.be4you.core.data.network.ws.api.mappers.toAsset
import co.be4you.core.data.network.ws.api.models.assets.CreateAssetRequestDto
import co.be4you.core.data.network.ws.api.models.assets.EditAssetRequestDto
import co.be4you.core.data.network.ws.api.utils.apiCallListMap
import co.be4you.core.data.network.ws.api.utils.apiCallMap
import co.be4you.core.data.network.ws.api.utils.apiCallUnit
import co.be4you.core.domain.models.Asset

class WSAssetService(
    private val assetApi: AssetApi
) : AssetService {

    override suspend fun getAssetsByFloorMap(floorMapId: Long): Result<List<Asset>> =
        apiCallListMap(
            call = { assetApi.getAssetsByFloor(floorMapId) },
            errorMessage = "Failed to fetch assets",
            mapper = { it.toAsset() }
        )

    override suspend fun getAsset(id: Long): Result<Asset> =
        apiCallMap(
            call = { assetApi.getAssetById(id) },
            errorMessage = "Failed to fetch asset",
            mapper = { it.toAsset() }
        )

    override suspend fun deleteAsset(id: Long): Result<Unit> =
        apiCallUnit(
            call = { assetApi.deleteAsset(id) },
            errorMessage = "Failed to delete asset"
        )

    override suspend fun createAsset(asset: Asset): Result<Unit> =
        apiCallUnit(
            call = {
                assetApi.createAsset(
                    body = CreateAssetRequestDto(
                        name = asset.name,
                        x = asset.x,
                        y = asset.y,
                        floorMapId = asset.floorMapId,
                        active = asset.active,
                        color = asset.colorHex
                    )
                )
            },
            errorMessage = "Failed to create asset"
        )

    override suspend fun updateAsset(asset: Asset): Result<Unit> =
        apiCallUnit(
            call = {
                assetApi.updateAsset(
                    id = asset.id,
                    body = EditAssetRequestDto(
                        name = asset.name,
                        colorHex = asset.colorHex.orEmpty(),
                    ),
                )
            },
            errorMessage = "Failed to edit",
        )
}

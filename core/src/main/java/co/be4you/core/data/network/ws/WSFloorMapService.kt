package co.be4you.core.data.network.ws

import co.be4you.core.data.network.services.FloorMapService
import co.be4you.core.data.network.ws.api.FloorMapApi
import co.be4you.core.data.network.ws.api.mappers.toFloorMap
import co.be4you.core.domain.models.FloorMap

class WSFloorMapService(
    private val floorMapApi: FloorMapApi,
) : FloorMapService {
    override suspend fun getFloorMap(id: Long): Result<FloorMap> {
        try {
            val accessToken =
                Tokens.accessToken ?: return Result.failure(Throwable("No access token"))

            val result = floorMapApi.getFloorMap(
                id = id,
                token = "Bearer $accessToken"
            )

            when (result.isSuccessful) {
                true -> {
                    val body = result.body() ?: return Result.failure(Throwable("Body is null"))

                    return Result.success(body.toFloorMap())
                }

                false -> {
                    return Result.failure(Throwable(message = "Failed to fetch floor map"))
                }
            }
        } catch (error: Throwable) {
            return Result.failure(error)
        }
    }

    override suspend fun getFloorMaps(): Result<List<FloorMap>> {
        try {
            val accessToken =
                Tokens.accessToken ?: return Result.failure(Throwable("No access token"))

            val result = floorMapApi.getFloorMaps(token = "Bearer $accessToken")

            when (result.isSuccessful) {
                true -> {
                    val body = result.body() ?: return Result.failure(Throwable("Body is null"))

                    val floorMaps = body.map { it.toFloorMap() }

                    return Result.success(floorMaps)
                }

                false -> {
                    return Result.failure(Throwable(message = "Failed to fetch floor map"))
                }
            }
        } catch (error: Throwable) {
            return Result.failure(error)
        }
    }
}

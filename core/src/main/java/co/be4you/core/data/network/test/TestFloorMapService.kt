package co.be4you.core.data.network.test

import co.be4you.core.data.network.services.FloorMapService
import co.be4you.core.data.network.test.mappers.toFloorMap
import co.be4you.core.data.network.test.models.TestFloorMap
import co.be4you.core.domain.models.FloorMap
import kotlinx.coroutines.delay

const val FLOOR_MAP_ID = 1L

class TestFloorMapService : FloorMapService {

    val mockData = listOf(
        TestFloorMap(
            id = FLOOR_MAP_ID,
            name = "Test-1",
            imageUrl = "https://picsum.photos/id/1/5000/3333"
        )
    )

    override suspend fun getFloorMap(id: Long): Result<FloorMap> {
        delay(2000)

        val testFloorMap = mockData.firstOrNull { it.id == id }

        if (testFloorMap == null) return Result.failure(Exception("Floor map not found"))

        return Result.success(testFloorMap.toFloorMap())
    }

    override suspend fun getFloorMaps(): Result<List<FloorMap>> {
        delay(2000)

        val floorMaps = mockData.map { it.toFloorMap() }

        return Result.success(floorMaps)
    }
}

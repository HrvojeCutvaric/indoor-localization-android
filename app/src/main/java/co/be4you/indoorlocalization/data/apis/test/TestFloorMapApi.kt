package co.be4you.indoorlocalization.data.apis.test

import co.be4you.indoorlocalization.data.apis.FloorMapApi
import co.be4you.indoorlocalization.data.apis.test.mappers.toFloorMap
import co.be4you.indoorlocalization.data.apis.test.models.TestFloorMap
import co.be4you.indoorlocalization.domain.models.FloorMap
import kotlinx.coroutines.delay

const val FLOOR_MAP_ID = 1L

class TestFloorMapApi : FloorMapApi {

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
}

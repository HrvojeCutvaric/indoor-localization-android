package co.be4you.indoorlocalization.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Route : NavKey {

    @Serializable
    data object Registration : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Dashboard : Route
    @Serializable
    data class Assets(
        val floorMapId: Long,
        val floorMapName: String
    ):Route

    @Serializable
    data class AssetDetails(val assetId: Long): Route
}

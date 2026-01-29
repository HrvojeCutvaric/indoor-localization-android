package co.be4you.core.data.network.ws.api.models.asset_position_history

import com.google.gson.annotations.SerializedName

data class AssetPositonHistoryDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("assetId")
    val assetId: Long,
    @SerializedName("floorMapId")
    val floorMapId: Long,
    @SerializedName("x")
    val x: Double,
    @SerializedName("y")
    val y: Double,
    @SerializedName("dateTime")
    val dateTime: String
)

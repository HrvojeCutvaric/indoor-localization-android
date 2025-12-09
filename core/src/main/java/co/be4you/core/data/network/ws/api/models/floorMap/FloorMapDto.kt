package co.be4you.core.data.network.ws.api.models.floorMap

import com.google.gson.annotations.SerializedName

data class FloorMapDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("imageWidthPx")
    val imageWidthPx: Int?,
    @SerializedName("imageHeightPx")
    val imageHeightPx: Int?,
    @SerializedName("widthInMeters")
    val widthInMeters: Int,
    @SerializedName("heightInMeters")
    val heightInMeters: Int,
)
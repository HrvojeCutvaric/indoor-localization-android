package co.be4you.core.data.network.ws.api.models.zones

import com.google.gson.annotations.SerializedName

data class ZoneDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("points") val points: String,
    @SerializedName("floorMapId") val floorMapId: Long,
)

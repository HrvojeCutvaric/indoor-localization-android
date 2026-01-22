package co.be4you.core.domain.models

import com.google.gson.annotations.SerializedName

data class ZonePoint(
    @SerializedName("X") val x: Double,
    @SerializedName("Y") val y: Double,
    @SerializedName("OrdinalNumber") val ordinalNumber: Int,
)

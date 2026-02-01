package co.be4you.core.data.network.ws.api.models.zone_retention_history

import com.google.gson.annotations.SerializedName

data class ZoneRetentionHistoryDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("assetId")
    val assetId: Long,
    @SerializedName("zoneId")
    val zoneId: Long,
    @SerializedName("enterDateTime")
    val enterDateTime: String,
    @SerializedName("exitDateTime")
    val exitDateTime: String?,
    @SerializedName("retentionTime")
    val retentionTime: String?
)
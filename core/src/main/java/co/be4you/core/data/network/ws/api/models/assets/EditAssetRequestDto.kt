package co.be4you.core.data.network.ws.api.models.assets

import com.google.gson.annotations.SerializedName

data class EditAssetRequestDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("color")
    val colorHex: String,
)

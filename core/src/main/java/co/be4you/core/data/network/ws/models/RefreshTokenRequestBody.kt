package co.be4you.core.data.network.ws.models

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequestBody(
    @SerializedName("refreshToken")
    val refreshToken: String
)
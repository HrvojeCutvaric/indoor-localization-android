package co.be4you.core.data.network.ws.api.models.auth

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequestBody(
    @SerializedName("accessToken")
    val accessToken: String,

    @SerializedName("refreshToken")
    val refreshToken: String
)

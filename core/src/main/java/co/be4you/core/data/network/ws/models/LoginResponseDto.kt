package co.be4you.core.data.network.ws.models

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("accessToken")
    val accessToken: String?,
    @SerializedName("refreshToken")
    val refreshToken: String?,
    @SerializedName("userId")
    val userId: Long?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("email")
    val email: String?,
)

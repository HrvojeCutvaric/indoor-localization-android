package co.be4you.core.data.network.ws.api.models.auth

import com.google.gson.annotations.SerializedName

data class LoginRequestBody(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
)

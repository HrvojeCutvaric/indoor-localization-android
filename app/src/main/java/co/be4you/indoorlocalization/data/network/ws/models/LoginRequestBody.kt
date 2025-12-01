package co.be4you.indoorlocalization.data.network.ws.models

import com.google.gson.annotations.SerializedName

data class LoginRequestBody(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
)

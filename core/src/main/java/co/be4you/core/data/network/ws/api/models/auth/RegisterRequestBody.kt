package co.be4you.core.data.network.ws.api.models.auth

import com.google.gson.annotations.SerializedName

data class RegisterRequestBody(
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String
)

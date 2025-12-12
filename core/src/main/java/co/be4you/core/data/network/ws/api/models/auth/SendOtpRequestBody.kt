package co.be4you.core.data.network.ws.api.models.auth

import com.google.gson.annotations.SerializedName

data class SendOtpRequestBody(
    @SerializedName("email")
    val email: String,
)

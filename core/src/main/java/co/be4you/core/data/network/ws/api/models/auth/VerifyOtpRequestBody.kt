package co.be4you.core.data.network.ws.api.models.auth

import com.google.gson.annotations.SerializedName

data class VerifyOtpRequestBody(
    @SerializedName("email")
    val email: String,
    @SerializedName("otp")
    val otp: String,
)

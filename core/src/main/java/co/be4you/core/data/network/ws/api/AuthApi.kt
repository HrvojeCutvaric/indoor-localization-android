package co.be4you.core.data.network.ws.api

import co.be4you.core.data.network.ws.api.models.auth.LoginRequestBody
import co.be4you.core.data.network.ws.api.models.auth.LoginResponseDto
import co.be4you.core.data.network.ws.api.models.auth.RefreshTokenRequestBody
import co.be4you.core.data.network.ws.api.models.auth.RegisterRequestBody
import co.be4you.core.data.network.ws.api.models.auth.SendOtpRequestBody
import co.be4you.core.data.network.ws.api.models.auth.VerifyOtpRequestBody
import co.be4you.core.data.network.ws.api.models.utils.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/Auth/login")
    suspend fun login(
        @Body requestBody: LoginRequestBody,
    ): ApiResponse<LoginResponseDto>

    @POST("/api/Auth/register")
    suspend fun register(
        @Body requestBody: RegisterRequestBody,
    ): ApiResponse<Unit>

    @POST("/api/Auth/refresh")
    suspend fun refreshToken(
        @Body requestBody: RefreshTokenRequestBody
    ): ApiResponse<LoginResponseDto>

    @POST("/api/Auth/otp/send")
    suspend fun sendOtp(
        @Body requestBody: SendOtpRequestBody,
    ): ApiResponse<Unit>

    @POST("/api/Auth/otp/verify")
    suspend fun verifyOtp(
        @Body requestBody: VerifyOtpRequestBody,
    ): ApiResponse<LoginResponseDto>
}

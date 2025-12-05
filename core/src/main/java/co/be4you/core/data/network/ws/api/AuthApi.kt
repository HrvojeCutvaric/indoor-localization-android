package co.be4you.core.data.network.ws.api

import co.be4you.core.data.network.ws.api.models.LoginRequestBody
import co.be4you.core.data.network.ws.api.models.LoginResponseDto
import co.be4you.core.data.network.ws.api.models.RegisterRequestBody
import co.be4you.core.data.network.ws.models.RegisterRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/Auth/login")
    suspend fun login(
        @Body requestBody: LoginRequestBody,
    ): Response<LoginResponseDto>

    @POST("/api/Auth/register")
    suspend fun register(
        @Body requestBody: RegisterRequestBody,
    ): Response<Unit>

    @POST("/api/Auth/refresh")
    suspend fun refreshToken(
        @Body requestBody: RefreshTokenRequestBody
    ): Response<LoginResponseDto>
}

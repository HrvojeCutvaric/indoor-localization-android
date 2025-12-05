package co.be4you.core.data.network.ws

import co.be4you.core.data.network.ws.models.LoginRequestBody
import co.be4you.core.data.network.ws.models.LoginResponseDto
import co.be4you.core.data.network.ws.models.RefreshTokenRequestBody
import co.be4you.core.data.network.ws.models.RegisterRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
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

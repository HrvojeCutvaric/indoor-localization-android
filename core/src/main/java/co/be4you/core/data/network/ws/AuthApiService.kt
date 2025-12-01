package co.be4you.core.data.network.ws

import co.be4you.core.data.network.ws.models.LoginRequestBody
import co.be4you.core.data.network.ws.models.LoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/Auth/login")
    suspend fun login(
        @Body requestBody: LoginRequestBody,
    ): Response<LoginResponseDto>
}

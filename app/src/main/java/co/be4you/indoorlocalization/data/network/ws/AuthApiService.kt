package co.be4you.indoorlocalization.data.network.ws

import co.be4you.indoorlocalization.data.network.ws.models.LoginRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/Auth/login")
    suspend fun login(
        @Body requestBody: LoginRequestBody,
    ): Response<Unit>
}

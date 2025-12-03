package co.be4you.core.data.network.ws

import co.be4you.core.data.network.services.AuthService
import co.be4you.core.data.network.ws.api.AuthApi
import co.be4you.core.data.network.ws.api.mappers.toLoginResponse
import co.be4you.core.data.network.ws.api.models.LoginRequestBody
import co.be4you.core.data.network.ws.api.models.RegisterRequestBody
import co.be4you.core.domain.models.LoginResponse
import co.be4you.core.domain.utils.LoginThrowable

class WSAuthService(
    private val authApi: AuthApi
) : AuthService {

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        username: String,
        password: String
    ): Result<Unit> {
        val registerResult = authApi.register(
            requestBody = RegisterRequestBody(
                username = username,
                email = email,
                password = password,
                firstName = firstName,
                lastName = lastName,
            )
        )

        if (registerResult.isSuccessful.not()) {
            return Result.failure(Exception("Registration failed"))
        }

        return Result.success(Unit)
    }

    override suspend fun login(
        username: String,
        password: String
    ): Result<LoginResponse> {

        val loginResult = authApi.login(
            requestBody = LoginRequestBody(
                username = username,
                password = password
            )
        )

        if (loginResult.isSuccessful.not()) return Result.failure(LoginThrowable.IncorrectEmailPassword)

        val loginResponseDto =
            loginResult.body() ?: return Result.failure(Exception("Login response is null"))

        return Result.success(loginResponseDto.toLoginResponse())
    }
}

package co.be4you.core.data.network.ws

import co.be4you.core.data.network.AuthService
import co.be4you.core.data.network.ws.mappers.toLoginResponse
import co.be4you.core.data.network.ws.models.LoginRequestBody
import co.be4you.core.data.network.ws.models.RegisterRequestBody
import co.be4you.core.domain.models.LoginResponse
import co.be4you.core.domain.utils.LoginThrowable

class WSAuthService(
    private val authApiService: AuthApiService
) : AuthService {

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        username: String,
        password: String
    ): Result<Unit> {
        val registerResult = authApiService.register(
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

        val loginResult = authApiService.login(
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

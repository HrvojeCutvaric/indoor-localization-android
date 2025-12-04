package co.be4you.core.data.repositories

import co.be4you.core.data.network.services.AuthService
import co.be4you.core.data.network.ws.Tokens
import co.be4you.core.domain.models.LoginResponse

class AuthRepository(
    private val authService: AuthService,
) {

    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        username: String,
        password: String,
    ): Result<Unit> {
        return authService.register(
            firstName = firstName,
            lastName = lastName,
            email = email,
            username = username,
            password = password,
        )
    }

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        authService.login(
            username = username,
            password = password,
        ).fold(
            onSuccess = {
                Tokens.accessToken = it.accessToken
                return Result.success(it)
            },
            onFailure = {
                return Result.failure(it)
            }
        )
    }
}

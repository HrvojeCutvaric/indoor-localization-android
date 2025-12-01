package co.be4you.core.data.repositories

import co.be4you.core.data.network.AuthService
import co.be4you.core.domain.models.LoginResponse

class AuthRepository(
    private val authService: AuthService,
) {

    suspend fun register(email: String, password: String): Result<Unit> =
        authService.register(email = email, password = password)

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return authService.login(username = username, password = password)
    }
}

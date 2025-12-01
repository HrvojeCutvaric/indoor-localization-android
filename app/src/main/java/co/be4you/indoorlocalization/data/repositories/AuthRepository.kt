package co.be4you.indoorlocalization.data.repositories

import co.be4you.indoorlocalization.data.network.AuthService

class AuthRepository(
    private val authService: AuthService,
) {

    suspend fun register(email: String, password: String): Result<Unit> =
        authService.register(email = email, password = password)

    suspend fun login(username: String, password: String): Result<Unit> {
        return authService.login(email = username, password = password)
    }
}

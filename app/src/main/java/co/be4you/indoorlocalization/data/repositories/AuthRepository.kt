package co.be4you.indoorlocalization.data.repositories

import co.be4you.indoorlocalization.data.apis.AuthApi

class AuthRepository(
    private val authApi: AuthApi,
) {

    suspend fun register(email: String, password: String): Result<Unit> =
        authApi.register(email = email, password = password)
}

package co.be4you.core.data.network

import co.be4you.core.domain.models.LoginResponse

interface AuthService {

    suspend fun register(email: String, password: String): Result<Unit>

    suspend fun login(username: String, password: String): Result<LoginResponse>
}

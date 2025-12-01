package co.be4you.indoorlocalization.data.network.ws

import co.be4you.indoorlocalization.data.network.AuthService
import co.be4you.indoorlocalization.data.network.ws.models.LoginRequestBody
import co.be4you.indoorlocalization.domain.utils.LoginThrowable
import co.be4you.indoorlocalization.domain.utils.RegisterThrowable
import kotlinx.coroutines.delay

class WSAuthService(
    private val authApiService: AuthApiService
) : AuthService {

    private var users = mapOf(
        "test@gmail.com" to "qqqqqq",
        "test123@gmail.com" to "qqqqqq"
    )

    override suspend fun register(
        email: String,
        password: String
    ): Result<Unit> {
        delay(2000)

        if (users.containsKey(email).not()) {
            users = users + mapOf(email to password)
            return Result.success(Unit)
        }

        return Result.failure(RegisterThrowable.EmailExists)
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<Unit> {

        val isSuccessful =
            authApiService.login(LoginRequestBody(username = email, password = password)).isSuccessful

        return if (isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(LoginThrowable.IncorrectEmailPassword)
        }
    }
}

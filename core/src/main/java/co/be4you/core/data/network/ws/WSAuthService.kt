package co.be4you.core.data.network.ws

import co.be4you.core.data.network.AuthService
import co.be4you.core.data.network.ws.mappers.toLoginResponse
import co.be4you.core.data.network.ws.models.LoginRequestBody
import co.be4you.core.domain.models.LoginResponse
import co.be4you.core.domain.utils.LoginThrowable
import co.be4you.core.domain.utils.RegisterThrowable
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

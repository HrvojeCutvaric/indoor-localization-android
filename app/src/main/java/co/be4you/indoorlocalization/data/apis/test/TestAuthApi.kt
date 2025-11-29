package co.be4you.indoorlocalization.data.apis.test

import co.be4you.indoorlocalization.data.apis.AuthApi
import co.be4you.indoorlocalization.domain.utils.LoginThrowable
import co.be4you.indoorlocalization.domain.utils.RegisterThrowable
import kotlinx.coroutines.delay

class TestAuthApi : AuthApi {

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
        delay(2000)

        if (users.containsKey(email).not() || users[email] != password) {
            return Result.failure(LoginThrowable.IncorrectEmailPassword)
        }

        return Result.success(Unit)
    }
}

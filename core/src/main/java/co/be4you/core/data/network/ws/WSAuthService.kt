package co.be4you.core.data.network.ws

import co.be4you.core.data.network.AuthService
import co.be4you.core.data.network.ws.mappers.toLoginResponse
import co.be4you.core.data.network.ws.models.LoginRequestBody
import co.be4you.core.data.network.ws.models.RegisterRequestBody
import co.be4you.core.domain.models.LoginResponse
import co.be4you.core.domain.storage.TokenStorage
import co.be4you.core.domain.utils.LoginThrowable
import co.be4you.core.domain.utils.RegisterThrowable
import org.json.JSONObject


class WSAuthService(
    private val authApiService: AuthApiService,
    private val tokenStorage: TokenStorage
) : AuthService {

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        username: String,
        password: String
    ): Result<Unit> {

        val registerResult = authApiService.register(
            RegisterRequestBody(
                username = username,
                email = email,
                password = password,
                firstName = firstName,
                lastName = lastName
            )
        )

        if (!registerResult.isSuccessful) {

            val errorBody = registerResult.errorBody()?.string()
            val backendMessage = try {
                JSONObject(errorBody ?: "{}").getString("message")
            } catch (e: Exception) {
                null
            }

            return when (registerResult.code()) {
                409 -> {
                    if (backendMessage?.contains("username", ignoreCase = true) == true) {
                        Result.failure(RegisterThrowable.UsernameExists)
                    } else if (backendMessage?.contains("email", ignoreCase = true) == true) {
                        Result.failure(RegisterThrowable.EmailExists)
                    } else {
                        Result.failure(RegisterThrowable.EmailExists)
                    }
                }

                400 -> Result.failure(RegisterThrowable.InvalidEmail)

                else -> Result.failure(Exception("Registration failed"))
            }

        }

        return Result.success(Unit)
    }


    override suspend fun login(
        username: String,
        password: String
    ): Result<LoginResponse> {

        val loginResult = try {
            authApiService.login(
                requestBody = LoginRequestBody(
                    username = username,
                    password = password
                )
            )
        } catch (e: Exception) {
            return Result.failure(LoginThrowable.Generic)
        }

        if (!loginResult.isSuccessful) {
            return when (loginResult.code()) {
                400, 401 -> Result.failure(LoginThrowable.IncorrectEmailPassword)
                else -> Result.failure(LoginThrowable.Generic)
            }
        }

        val loginResponseDto =
            loginResult.body() ?: return Result.failure(LoginThrowable.Generic)

        tokenStorage.saveTokens(
            accessToken = loginResponseDto.accessToken.orEmpty(),
            refreshToken = loginResponseDto.refreshToken.orEmpty()
        )

        android.util.Log.d(
            "TOKEN_TEST",
            "Saved -> access=${tokenStorage.getAccessToken()}, refresh=${tokenStorage.getRefreshToken()}"
        )


        /*if (loginResponseDto.accessToken.isNullOrBlank() ||
            loginResponseDto.refreshToken.isNullOrBlank()
        ) {
            return Result.failure(LoginThrowable.Generic)
        }*/



        return Result.success(loginResponseDto.toLoginResponse())
    }

}


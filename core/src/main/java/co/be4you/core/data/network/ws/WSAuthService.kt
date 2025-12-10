package co.be4you.core.data.network.ws

import co.be4you.core.data.network.services.AuthService
import co.be4you.core.data.network.ws.api.AuthApi
import co.be4you.core.data.network.ws.api.mappers.toLoginResponse
import co.be4you.core.data.network.ws.api.models.auth.LoginRequestBody
import co.be4you.core.data.network.ws.api.models.auth.RegisterRequestBody
import co.be4you.core.data.network.ws.api.models.auth.SendOtpRequestBody
import co.be4you.core.data.network.ws.api.models.auth.VerifyOtpRequestBody
import co.be4you.core.domain.models.LoginResponse
import co.be4you.core.domain.utils.LoginThrowable
import co.be4you.core.domain.utils.RegisterThrowable
import co.be4you.core.domain.utils.VerifyOtpThrowable
import org.json.JSONObject


class WSAuthService(
    private val authApi: AuthApi
) : AuthService {

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        username: String,
        password: String
    ): Result<Unit> {
        val registerResult = authApi.register(
            requestBody = RegisterRequestBody(
                username = username,
                email = email,
                password = password,
                firstName = firstName,
                lastName = lastName,
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
            authApi.login(
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


        return Result.success(loginResponseDto.toLoginResponse())
    }

    override suspend fun requestOtp(email: String): Result<Unit> {
        try {
            val result = authApi.sendOtp(requestBody = SendOtpRequestBody(email = email))

            return when (result.isSuccessful) {
                true -> {
                    Result.success(Unit)
                }

                false -> {
                    Result.failure(Throwable(message = "Failed to send otp"))
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    override suspend fun verifyOtp(
        email: String,
        otp: String
    ): Result<LoginResponse> {
        try {
            val result =
                authApi.verifyOtp(requestBody = VerifyOtpRequestBody(email = email, otp = otp))

            return when (result.isSuccessful) {
                true -> {
                    val body = result.body() ?: return Result.failure(Throwable("Body is null"))

                    return Result.success(body.toLoginResponse())
                }

                false -> {
                    Result.failure(VerifyOtpThrowable.InvalidOtp)
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            return Result.failure(e)
        }
    }
}

package co.be4you.core.data.network.ws

import co.be4you.core.data.network.services.AuthService
import co.be4you.core.data.network.ws.api.AuthApi
import co.be4you.core.data.network.ws.api.mappers.toLoginResponse
import co.be4you.core.data.network.ws.api.models.auth.LoginRequestBody
import co.be4you.core.data.network.ws.api.models.auth.RegisterRequestBody
import co.be4you.core.data.network.ws.api.models.auth.SendOtpRequestBody
import co.be4you.core.data.network.ws.api.models.auth.VerifyOtpRequestBody
import co.be4you.core.data.network.ws.api.utils.apiCallMap
import co.be4you.core.data.network.ws.api.utils.apiCallUnit
import co.be4you.core.domain.models.LoginResponse
import co.be4you.core.domain.utils.LoginThrowable
import co.be4you.core.domain.utils.RegisterThrowable


class WSAuthService(
    private val authApi: AuthApi
) : AuthService {

    override suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        username: String,
        password: String
    ): Result<Unit> =
        apiCallUnit(
            call = {
                authApi.register(
                    requestBody = RegisterRequestBody(
                        username = username,
                        email = email,
                        password = password,
                        firstName = firstName,
                        lastName = lastName,
                    )
                )
            },
            errorMessage = "Registration failed",
            onFailure = { resp ->
                val msg = resp.message.orEmpty()

                when {
                    msg.contains("username", ignoreCase = true) -> RegisterThrowable.UsernameExists
                    msg.contains("email", ignoreCase = true) -> RegisterThrowable.EmailExists
                    msg.contains(
                        "invalid email",
                        ignoreCase = true
                    ) -> RegisterThrowable.InvalidEmail

                    else -> Throwable(msg.ifBlank { "Registration failed" })
                }
            }
        )

    override suspend fun login(
        username: String,
        password: String
    ): Result<LoginResponse> = apiCallMap(
        call = {
            authApi.login(
                requestBody = LoginRequestBody(
                    username = username,
                    password = password
                )
            )
        },
        errorMessage = "Login failed",
        onFailure = { resp ->
            val msg = resp.message.orEmpty()
            when {
                msg.contains("incorrect", ignoreCase = true) ||
                        msg.contains(
                            "invalid",
                            ignoreCase = true
                        ) -> LoginThrowable.IncorrectEmailPassword

                else -> LoginThrowable.Generic
            }
        },
        mapper = { dto -> dto.toLoginResponse() }
    )

    override suspend fun requestOtp(email: String): Result<Unit> =
        apiCallUnit(
            call = { authApi.sendOtp(requestBody = SendOtpRequestBody(email = email)) },
            errorMessage = "Failed to send otp"
        )

    override suspend fun verifyOtp(
        email: String,
        otp: String
    ): Result<LoginResponse> =
        apiCallMap(
            call = {
                authApi.verifyOtp(
                    requestBody = VerifyOtpRequestBody(
                        email = email,
                        otp = otp,
                    )
                )
            },
            errorMessage = "Failed to verify otp",
            mapper = { it.toLoginResponse() }
        )
}

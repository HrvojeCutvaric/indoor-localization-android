package co.be4you.core.data.repositories

import android.util.Log
import co.be4you.core.data.network.services.AuthService
import co.be4you.core.domain.models.LoginResponse
import co.be4you.core.domain.storage.AppEncryptedSharedPreferences

class AuthRepository(
    private val authService: AuthService,
    private val appEncryptedSharedPreferences: AppEncryptedSharedPreferences
) {

    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        username: String,
        password: String,
    ): Result<Unit> {
        return authService.register(
            firstName = firstName,
            lastName = lastName,
            email = email,
            username = username,
            password = password,
        )
    }

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        val result = authService.login(username, password)

        return result.onSuccess { loginResponse ->
            appEncryptedSharedPreferences.saveTokens(
                accessToken = loginResponse.accessToken,
                refreshToken = loginResponse.refreshToken
            )
            Log.d("TOKEN_TEST", "Saved access=${loginResponse.accessToken.take(20)}...")
        }
    }

    suspend fun requestOtp(email: String): Result<Unit> {
        return authService.requestOtp(email)
    }

    suspend fun verifyOtp(email: String, code: String): Result<LoginResponse> {
        val result = authService.verifyOtp(email = email, otp = code)

        return result.onSuccess { loginResponse ->
            appEncryptedSharedPreferences.saveTokens(
                accessToken = loginResponse.accessToken,
                refreshToken = loginResponse.refreshToken
            )
        }
    }
}

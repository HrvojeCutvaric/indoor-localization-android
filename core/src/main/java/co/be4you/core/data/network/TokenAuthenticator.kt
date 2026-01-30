package co.be4you.core.data.network

import android.util.Log
import co.be4you.core.data.network.ws.api.AuthApi
import co.be4you.core.data.network.ws.api.models.auth.RefreshTokenRequestBody
import co.be4you.core.domain.storage.AppEncryptedSharedPreferences
import co.be4you.core.domain.utils.Constants
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val appEncryptedSharedPreferences: AppEncryptedSharedPreferences,
    private val authApi: AuthApi,
) : Authenticator {

    companion object {
        private const val TAG = "TOKEN_AUTH"
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) {
            Log.d(TAG, "Too many attempts, aborting.")
            return null
        }

        synchronized(this) {
            val currentAccessToken = appEncryptedSharedPreferences.getAccessToken()
            val refreshToken = appEncryptedSharedPreferences.getRefreshToken()

            if (refreshToken.isNullOrBlank()) {
                Log.d(TAG, "No refresh token, cannot refresh.")
                appEncryptedSharedPreferences.clearTokens()
                return null
            }

            val requestToken = response.request.header(Constants.AUTHORIZATION_HEADER)
                ?.removePrefix(Constants.BEARER_TOKEN_PREFIX)
                ?.trim()

            if (!currentAccessToken.isNullOrBlank() && currentAccessToken != requestToken) {
                Log.d(TAG, "Token already refreshed by another call, reusing it.")
                return response.request.newBuilder()
                    .header(
                        name = Constants.AUTHORIZATION_HEADER,
                        value = "${Constants.BEARER_TOKEN_PREFIX}$currentAccessToken"
                    )
                    .build()
            }

            return try {
                Log.d(TAG, "Refreshing token...")

                val refreshResponse = runBlocking {
                    authApi.refreshToken(
                        RefreshTokenRequestBody(
                            accessToken = currentAccessToken.orEmpty(),
                            refreshToken = refreshToken
                        )
                    )
                }

                if (refreshResponse.success.not()) {
                    Log.d(TAG, "Refresh failed with code ${refreshResponse.errorCode}")
                    appEncryptedSharedPreferences.clearTokens()
                    null
                } else {
                    val refreshData = refreshResponse.data
                    if (refreshData == null) {
                        Log.d(TAG, "Refresh data null.")
                        appEncryptedSharedPreferences.clearTokens()
                        null
                    } else {
                        val newAccess = refreshData.accessToken.orEmpty()
                        val newRefresh = refreshData.refreshToken.orEmpty()

                        appEncryptedSharedPreferences.saveTokens(newAccess, newRefresh)

                        Log.d(TAG, "Refresh success, retrying original request.")

                        response.request.newBuilder()
                            .header(
                                name = Constants.AUTHORIZATION_HEADER,
                                value = "${Constants.BEARER_TOKEN_PREFIX}$newAccess",
                            )
                            .build()
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Exception during refresh: ${e.message}")
                null
            }
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}

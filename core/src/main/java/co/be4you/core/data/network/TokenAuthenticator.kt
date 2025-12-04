package co.be4you.core.data.network

import android.util.Log
import co.be4you.core.data.network.ws.AuthApiService
import co.be4you.core.data.network.ws.models.RefreshTokenRequestBody
import co.be4you.core.domain.storage.AppEncryptedSharedPreferences
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TokenAuthenticator(
    private val appEncryptedSharedPreferences: AppEncryptedSharedPreferences
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) {
            Log.d("TOKEN_AUTH", "Too many attempts, aborting.")
            return null
        }

        synchronized(this) {
            val currentAccessToken = appEncryptedSharedPreferences.getAccessToken()
            val refreshToken = appEncryptedSharedPreferences.getRefreshToken()

            if (refreshToken.isNullOrBlank()) {
                Log.d("TOKEN_AUTH", "No refresh token, cannot refresh.")
                appEncryptedSharedPreferences.clearTokens()
                return null
            }

            val requestToken = response.request.header("Authorization")
                ?.removePrefix("Bearer ")
                ?.trim()

            if (!currentAccessToken.isNullOrBlank() && currentAccessToken != requestToken) {
                Log.d("TOKEN_AUTH", "Token already refreshed by another call, reusing it.")
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentAccessToken")
                    .build()
            }

            return try {
                Log.d("TOKEN_AUTH", "Refreshing token...")

                val client = OkHttpClient.Builder().build()

                val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:5000/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val authApiService = retrofit.create(AuthApiService::class.java)

                val refreshCall = authApiService.refreshTokenSync(
                    RefreshTokenRequestBody(
                        accessToken = currentAccessToken.orEmpty(),
                        refreshToken = refreshToken
                    )
                )

                val refreshResponse = refreshCall.execute()

                if (!refreshResponse.isSuccessful) {
                    Log.d("TOKEN_AUTH", "Refresh failed with code ${refreshResponse.code()}")
                    appEncryptedSharedPreferences.clearTokens()
                    null
                } else {
                    val body = refreshResponse.body()
                    if (body == null) {
                        Log.d("TOKEN_AUTH", "Refresh body null.")
                        appEncryptedSharedPreferences.clearTokens()
                        null
                    } else {
                        val newAccess = body.accessToken.orEmpty()
                        val newRefresh = body.refreshToken.orEmpty()

                        appEncryptedSharedPreferences.saveTokens(newAccess, newRefresh)

                        Log.d("TOKEN_AUTH", "Refresh success, retrying original request.")

                        response.request.newBuilder()
                            .header("Authorization", "Bearer $newAccess")
                            .build()
                    }
                }
            } catch (e: Exception) {
                Log.d("TOKEN_AUTH", "Exception during refresh: ${e.message}")
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

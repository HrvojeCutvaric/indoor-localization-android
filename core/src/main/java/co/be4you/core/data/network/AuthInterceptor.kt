package co.be4you.core.data.network

import co.be4you.core.domain.storage.AppEncryptedSharedPreferences
import co.be4you.core.domain.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val appEncryptedSharedPreferences: AppEncryptedSharedPreferences
) : Interceptor {

    companion object {
        private const val TAG = "AUTH_INTERCEPTOR"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val accessToken = appEncryptedSharedPreferences.getAccessToken()

        if (accessToken.isNullOrEmpty()) {
            return chain.proceed(originalRequest)
        }

        val newRequest = originalRequest.newBuilder()
            .header(Constants.AUTHORIZATION_HEADER, "${Constants.BEARER_TOKEN_PREFIX}$accessToken")
            .build()

        return chain.proceed(newRequest)
    }
}

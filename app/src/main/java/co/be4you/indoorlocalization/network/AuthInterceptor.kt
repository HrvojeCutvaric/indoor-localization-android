package co.be4you.indoorlocalization.network

import co.be4you.core.domain.storage.TokenStorage
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenStorage: TokenStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = tokenStorage.getAccessToken()

        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        if (!accessToken.isNullOrEmpty()) {
            requestBuilder.header("Authorization", "Bearer $accessToken")
        }

        val newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}

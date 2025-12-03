package co.be4you.core.data.network

import co.be4you.core.domain.storage.TokenStorage
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenStorage: TokenStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val accessToken = tokenStorage.getAccessToken()

        if (accessToken.isNullOrEmpty()) {
            return chain.proceed(originalRequest)
        }

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        android.util.Log.d("AUTH_INTERCEPTOR", "Adding token to request ${newRequest.url}")

        return chain.proceed(newRequest)
    }
}

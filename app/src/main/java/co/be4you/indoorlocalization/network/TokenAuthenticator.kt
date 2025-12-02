package co.be4you.indoorlocalization.network

import co.be4you.core.data.network.ws.AuthApiService
import co.be4you.core.data.network.ws.models.RefreshTokenRequestBody
import co.be4you.core.domain.storage.TokenStorage
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val apiService: AuthApiService,
    private val tokenStorage: TokenStorage
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        synchronized(this) {
            val currentAccess = tokenStorage.getAccessToken()
            val failedToken = response.request.header("Authorization")?.removePrefix("Bearer ")

            if (currentAccess != null && currentAccess != failedToken) {
                return newRequestWithToken(response.request, currentAccess)
            }

            val refreshToken = tokenStorage.getRefreshToken() ?: return null


            val refreshResponse = apiService
                .refreshTokenSync(RefreshTokenRequestBody(refreshToken))
                .execute()


            if (!refreshResponse.isSuccessful) {
                tokenStorage.clearTokens()
                return null
            }

            val body = refreshResponse.body() ?: return null


            tokenStorage.saveTokens(
                accessToken = body.accessToken.orEmpty(),
                refreshToken = body.refreshToken.orEmpty()
            )


            return newRequestWithToken(response.request, body.accessToken.orEmpty())
        }
    }

    private fun newRequestWithToken(request: Request, token: String): Request {
        return request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
    }
}

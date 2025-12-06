package co.be4you.core.domain.storage

interface AppEncryptedSharedPreferences {
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun saveTokens(accessToken: String, refreshToken: String)
    fun clearTokens()
}

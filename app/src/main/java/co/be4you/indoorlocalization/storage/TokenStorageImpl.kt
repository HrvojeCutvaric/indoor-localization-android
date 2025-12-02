package co.be4you.indoorlocalization.storage

import android.content.SharedPreferences
import co.be4you.core.domain.storage.TokenStorage

class TokenStorageImpl(
    private val sharedPreferences: SharedPreferences
) : TokenStorage {

    override fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit()
            .putString("access_token", accessToken)
            .putString("refresh_token", refreshToken)
            .apply()
    }

    override fun getAccessToken(): String? =
        sharedPreferences.getString("access_token", null)

    override fun getRefreshToken(): String? =
        sharedPreferences.getString("refresh_token", null)

    override fun clearTokens() {
        sharedPreferences.edit()
            .remove("access_token")
            .remove("refresh_token")
            .apply()
    }
}

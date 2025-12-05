package co.be4you.indoorlocalization.storage

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import co.be4you.core.domain.storage.AppEncryptedSharedPreferences

class AppEncryptedSharedPreferencesImpl(context: Context) : AppEncryptedSharedPreferences {

    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val FILE_NAME = "secure_auth_prefs"
    }

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun getAccessToken(): String? =
        prefs.getString(ACCESS_TOKEN_KEY, null)

    override fun getRefreshToken(): String? =
        prefs.getString(REFRESH_TOKEN_KEY, null)

    override fun saveTokens(accessToken: String, refreshToken: String) {
        prefs.edit {
            putString(ACCESS_TOKEN_KEY, accessToken)
                .putString(REFRESH_TOKEN_KEY, refreshToken)
        }
    }

    override fun clearTokens() {
        prefs.edit { clear() }
    }
}

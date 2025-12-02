package co.be4you.indoorlocalization.di

import android.content.Context
import android.content.SharedPreferences
import co.be4you.core.data.network.AuthService
import co.be4you.core.data.network.FloorMapApi
import co.be4you.core.data.network.test.TestFloorMapApi
import co.be4you.core.data.network.ws.AuthApiService
import co.be4you.core.data.network.ws.WSAuthService
import co.be4you.core.data.repositories.AuthRepository
import co.be4you.core.data.repositories.FloorMapRepository
import co.be4you.core.domain.use_case.RegisterUseCase
import co.be4you.indoorlocalization.viewmodel.dashboard.DashboardViewModel
import co.be4you.indoorlocalization.viewmodel.login.LoginViewModel
import co.be4you.indoorlocalization.viewmodel.main.MainViewModel
import co.be4you.indoorlocalization.viewmodel.registration.RegistrationViewModel
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import co.be4you.core.domain.storage.TokenStorage
import co.be4you.indoorlocalization.storage.TokenStorageImpl
import co.be4you.indoorlocalization.network.AuthInterceptor
import co.be4you.indoorlocalization.network.TokenAuthenticator


val modules = module {
    singleOf(::WSAuthService).bind<AuthService>()
    singleOf(::AuthRepository).bind<AuthRepository>()
    singleOf(::TestFloorMapApi).bind<FloorMapApi>()
    singleOf(::FloorMapRepository).bind<FloorMapRepository>()

    viewModelOf(::MainViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::DashboardViewModel)

    factoryOf(::RegisterUseCase)

    /*single<SharedPreferences> {
        val context = get<Context>()


        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }*/





    /*single<TokenStorage> {
        TokenStorageImpl(get())
    }

    single {
        AuthInterceptor(
            tokenStorage = get()
        )
    }

    single {
        TokenAuthenticator(
            apiService = get<AuthApiService>(),
            tokenStorage = get()
        )
    }*/

    single {
        OkHttpClient.Builder()

            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        Retrofit
            .Builder()
            .baseUrl("http://10.0.2.2:5001")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(AuthApiService::class.java) }
}

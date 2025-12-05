package co.be4you.indoorlocalization.di

import co.be4you.core.data.network.AuthInterceptor
import co.be4you.core.data.network.services.AuthService
import co.be4you.core.data.network.services.FloorMapService
import co.be4you.core.data.network.TokenAuthenticator
import co.be4you.core.data.network.test.TestFloorMapService
import co.be4you.core.data.network.ws.api.AuthApi
import co.be4you.core.data.network.ws.WSAuthService
import co.be4you.core.data.repositories.AuthRepository
import co.be4you.core.data.repositories.FloorMapRepository
import co.be4you.core.domain.storage.AppEncryptedSharedPreferences
import co.be4you.core.domain.use_case.RegisterUseCase
import co.be4you.indoorlocalization.storage.AppEncryptedSharedPreferencesImpl
import co.be4you.indoorlocalization.viewmodel.dashboard.DashboardViewModel
import co.be4you.indoorlocalization.viewmodel.login.LoginViewModel
import co.be4you.indoorlocalization.viewmodel.main.MainViewModel
import co.be4you.indoorlocalization.viewmodel.registration.RegistrationViewModel
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

enum class RetrofitType {
    Default, Authorized
}

val modules = module {
    singleOf(::WSAuthService).bind<AuthService>()
    singleOf(::AuthRepository).bind<AuthRepository>()
    singleOf(::TestFloorMapService).bind<FloorMapService>()
    singleOf(::FloorMapRepository).bind<FloorMapRepository>()
    single<AppEncryptedSharedPreferences> { AppEncryptedSharedPreferencesImpl(androidContext()) }
    singleOf(::AuthInterceptor).bind<AuthInterceptor>()
    singleOf(::TokenAuthenticator).bind<TokenAuthenticator>()

    viewModelOf(::MainViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::DashboardViewModel)

    factoryOf(::RegisterUseCase)

    single(named(RetrofitType.Default)) {
        createRetrofit(
            okHttpClient = createDefaultOkHttpClient().build()
        )
    }
    single(named(RetrofitType.Authorized)) {
        createRetrofit(
            okHttpClient = createDefaultOkHttpClient()
                .addInterceptor(get<AuthInterceptor>())
                .authenticator(get<TokenAuthenticator>())
                .build()
        )
    }

    single { get<Retrofit>(named(RetrofitType.Default)).create(AuthApiService::class.java) }
}

private fun createDefaultOkHttpClient(): OkHttpClient.Builder =
    OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)


private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit
        .Builder()
        .baseUrl("http://10.0.2.2:5000/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

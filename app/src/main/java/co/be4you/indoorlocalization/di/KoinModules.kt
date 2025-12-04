package co.be4you.indoorlocalization.di

import co.be4you.core.data.network.AuthInterceptor
import co.be4you.core.data.network.AuthService
import co.be4you.core.data.network.TokenAuthenticator
import org.koin.android.ext.koin.androidContext
import co.be4you.core.data.network.FloorMapApi
import co.be4you.core.data.network.test.TestFloorMapApi
import co.be4you.core.data.network.ws.AuthApiService
import co.be4you.core.data.network.ws.WSAuthService
import co.be4you.core.data.repositories.AuthRepository
import co.be4you.core.data.repositories.FloorMapRepository
import co.be4you.core.domain.storage.TokenStorage
import co.be4you.core.domain.use_case.RegisterUseCase
import co.be4you.indoorlocalization.storage.EncryptedTokenStorage
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

val modules = module {

    viewModelOf(::MainViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::DashboardViewModel)

    factoryOf(::RegisterUseCase)

    single<TokenStorage> { EncryptedTokenStorage(androidContext()) }

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(AuthInterceptor(get()))
            .authenticator(TokenAuthenticator(get()))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { get<Retrofit>().create(AuthApiService::class.java) }

    single<AuthService> { WSAuthService(get()) }

    single { AuthRepository(get(), get()) }

    singleOf(::TestFloorMapApi).bind<FloorMapApi>()
    singleOf(::FloorMapRepository).bind<FloorMapRepository>()
}


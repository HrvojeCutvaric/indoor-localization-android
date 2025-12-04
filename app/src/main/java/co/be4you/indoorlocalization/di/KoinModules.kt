package co.be4you.indoorlocalization.di

import co.be4you.core.data.network.services.AuthService
import co.be4you.core.data.network.services.FloorMapService
import co.be4you.core.data.network.ws.WSAuthService
import co.be4you.core.data.network.ws.WSFloorMapService
import co.be4you.core.data.network.ws.api.AuthApi
import co.be4you.core.data.network.ws.api.FloorMapApi
import co.be4you.core.data.repositories.AuthRepository
import co.be4you.core.data.repositories.FloorMapRepository
import co.be4you.core.domain.use_case.RegisterUseCase
import co.be4you.core.domain.utils.Constants
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
    singleOf(::WSAuthService).bind<AuthService>()
    singleOf(::AuthRepository).bind<AuthRepository>()
    singleOf(::WSFloorMapService).bind<FloorMapService>()
    singleOf(::FloorMapRepository).bind<FloorMapRepository>()

    viewModelOf(::MainViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::DashboardViewModel)

    factoryOf(::RegisterUseCase)

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
            .baseUrl(Constants.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(AuthApi::class.java) }
    single { get<Retrofit>().create(FloorMapApi::class.java) }
}

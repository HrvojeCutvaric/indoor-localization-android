package co.be4you.indoorlocalization.di

import co.be4you.core.data.network.AuthInterceptor
import co.be4you.core.data.network.TokenAuthenticator
import co.be4you.core.data.network.services.AssetService
import co.be4you.core.data.network.services.AssetTrackingService
import co.be4you.core.data.network.services.AuthService
import co.be4you.core.data.network.services.FloorMapService
import co.be4you.core.data.network.services.ZoneService
import co.be4you.core.data.network.ws.WSAssetService
import co.be4you.core.data.network.ws.WSAuthService
import co.be4you.core.data.network.ws.WSFloorMapService
import co.be4you.core.data.network.ws.WSZoneService
import co.be4you.core.data.network.ws.api.AssetApi
import co.be4you.core.data.network.ws.api.AuthApi
import co.be4you.core.data.network.ws.api.FloorMapApi
import co.be4you.core.data.network.ws.api.ZoneApi
import co.be4you.core.data.network.ws.mqtt.MqttAssetTrackingService
import co.be4you.core.data.repositories.AssetRepository
import co.be4you.core.data.repositories.AssetTrackingRepository
import co.be4you.core.data.repositories.AuthRepository
import co.be4you.core.data.repositories.FloorMapRepository
import co.be4you.core.data.repositories.ZoneRepository
import co.be4you.core.domain.storage.AppEncryptedSharedPreferences
import co.be4you.core.domain.use_case.RegisterUseCase
import co.be4you.core.domain.utils.Constants
import co.be4you.core.domain.utils.login.LoginHandler
import co.be4you.core.navigation.AppNavigator
import co.be4you.indoorlocalization.storage.AppEncryptedSharedPreferencesImpl
import co.be4you.indoorlocalization.viewmodel.assetdetail.AssetDetailViewModel
import co.be4you.indoorlocalization.viewmodel.assets.AssetsViewModel
import co.be4you.indoorlocalization.viewmodel.dashboard.DashboardViewModel
import co.be4you.indoorlocalization.viewmodel.login.LoginViewModel
import co.be4you.indoorlocalization.viewmodel.main.MainViewModel
import co.be4you.indoorlocalization.viewmodel.registration.RegistrationViewModel
import co.be4you.otp_login.OtpHandler
import co.be4you.otp_login.OtpLoginUiAction
import co.be4you.otp_login.OtpLoginUiState
import co.be4you.password_login.PasswordHandler
import co.be4you.password_login.PasswordLoginUiAction
import co.be4you.password_login.PasswordLoginUiState
import com.google.gson.Gson
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
    singleOf(::WSFloorMapService).bind<FloorMapService>()
    singleOf(::FloorMapRepository).bind<FloorMapRepository>()
    singleOf(::WSAssetService).bind<AssetService>()
    singleOf(::AssetRepository).bind<AssetRepository>()
    single<AppEncryptedSharedPreferences> { AppEncryptedSharedPreferencesImpl(androidContext()) }
    singleOf(::AuthInterceptor).bind<AuthInterceptor>()
    singleOf(::TokenAuthenticator).bind<TokenAuthenticator>()
    singleOf(::PasswordHandler).bind<LoginHandler<PasswordLoginUiState, PasswordLoginUiAction>>()
    singleOf(::OtpHandler).bind<LoginHandler<OtpLoginUiState, OtpLoginUiAction>>()
    singleOf(::AppNavigator).bind<AppNavigator>()
    singleOf(::MqttAssetTrackingService).bind<AssetTrackingService>()
    singleOf(::AssetTrackingRepository).bind<AssetTrackingRepository>()
    singleOf(::WSZoneService).bind<ZoneService>()
    singleOf(::ZoneRepository).bind<ZoneRepository>()
    single { Gson() }

    viewModelOf(::MainViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::AssetsViewModel)
    viewModelOf(::AssetDetailViewModel)

    factoryOf(::RegisterUseCase)

    single<List<LoginHandler<*, *>>> {
        listOf(
            get<PasswordHandler>(),
            get<OtpHandler>(),
        )
    }

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

    single { get<Retrofit>(named(RetrofitType.Default)).create(AuthApi::class.java) }
    single { get<Retrofit>(named(RetrofitType.Authorized)).create(FloorMapApi::class.java) }
    single { get<Retrofit>(named(RetrofitType.Authorized)).create(AssetApi::class.java) }
    single { get<Retrofit>(named(RetrofitType.Authorized)).create(ZoneApi::class.java) }
}

private fun createDefaultOkHttpClient(): OkHttpClient.Builder =
    OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)


private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit
        .Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

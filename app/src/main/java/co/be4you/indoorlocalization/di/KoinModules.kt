package co.be4you.indoorlocalization.di

import co.be4you.indoorlocalization.data.apis.AuthApi
import co.be4you.indoorlocalization.data.apis.FloorMapApi
import co.be4you.indoorlocalization.data.apis.test.TestAuthApi
import co.be4you.indoorlocalization.data.apis.test.TestFloorMapApi
import co.be4you.indoorlocalization.data.repositories.AuthRepository
import co.be4you.indoorlocalization.data.repositories.FloorMapRepository
import co.be4you.indoorlocalization.domain.use_case.RegisterUseCase
import co.be4you.indoorlocalization.viewmodel.login.LoginViewModel
import co.be4you.indoorlocalization.viewmodel.main.MainViewModel
import co.be4you.indoorlocalization.viewmodel.registration.RegistrationViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val modules = module {
    singleOf(::TestAuthApi).bind<AuthApi>()
    singleOf(::AuthRepository).bind<AuthRepository>()
    singleOf(::TestFloorMapApi).bind<FloorMapApi>()
    singleOf(::FloorMapRepository).bind<FloorMapRepository>()

    viewModelOf(::MainViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::LoginViewModel)

    factoryOf(::RegisterUseCase)
}

package co.be4you.indoorlocalization.di

import co.be4you.indoorlocalization.viewmodel.registration.RegistrationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::RegistrationViewModel)
}

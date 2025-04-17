package com.riders.thelab.feature.koin.di

import com.riders.thelab.feature.koin.data.remote.ApiImpl
import com.riders.thelab.feature.koin.data.RepositoryImpl
import com.riders.thelab.feature.koin.ui.KoinViewModel
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@Module
@ComponentScan("org.koin.sample")

object KoinModule {
    val appModule = module {
        /*single<ApiImpl> { ApiImpl() }
        single<RepositoryImpl> { RepositoryImpl(get()) }
        viewModel { KoinViewModel(get()) }*/

        singleOf(::ApiImpl)
        singleOf(::RepositoryImpl) { bind() }

        viewModelOf(::KoinViewModel)
    }
}
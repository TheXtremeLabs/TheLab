package com.riders.thelab.feature.koin.di

import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.core.ui.data.local.UiRepository
import com.riders.thelab.core.ui.data.local.preferences.IPreferences
import com.riders.thelab.core.ui.data.local.preferences.PreferencesImpl
import com.riders.thelab.feature.koin.data.IRepository
import com.riders.thelab.feature.koin.data.RepositoryImpl
import com.riders.thelab.feature.koin.data.remote.ApiImpl
import com.riders.thelab.feature.koin.data.remote.IApi
import com.riders.thelab.feature.koin.ui.KoinViewModel
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@ComponentScan(
    value = [
        "com.riders.thelab.feature.koin",
        "com.riders.thelab.core.ui"
    ]
)
@Module
object KoinModule {
    val appModule = module {
        singleOf(::PreferencesImpl) { bind<IPreferences>() }
        singleOf(::UiRepository) { bind<IUiRepository>() }

        singleOf(::ApiImpl) { bind<IApi>() }
        factoryOf(::RepositoryImpl) { bind<IRepository>() }

        viewModelOf(::KoinViewModel)
    }
}
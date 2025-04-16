package com.riders.thelab.feature.koin

import com.riders.thelab.core.data.RepositoryImpl
import com.riders.thelab.core.data.remote.ApiImpl
import com.riders.thelab.feature.koin.di.KoinModule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.definition
import org.koin.test.verify.injectedParameters
import org.koin.test.verify.verify

class KoinCheckModulesTest : KoinTest {

    /**
     * Checking your modules
     * The verify() function allow to verify the given Koin modules
     */
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAllModules() {
        KoinModule.appModule.verify(
            injections = injectedParameters(
                definition<RepositoryImpl>(ApiImpl::class)
            )
        )
    }
}
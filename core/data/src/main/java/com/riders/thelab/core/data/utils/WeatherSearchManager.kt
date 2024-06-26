/*
package com.riders.thelab.core.data.utils

import android.annotation.SuppressLint
import android.content.Context
import androidx.appsearch.app.AppSearchSession
import androidx.appsearch.app.PutDocumentsRequest
import androidx.appsearch.app.SearchSpec
import androidx.appsearch.app.SetSchemaRequest
import androidx.appsearch.localstorage.LocalStorage
import androidx.appsearch.platformstorage.PlatformStorage
import com.google.common.util.concurrent.ListenableFuture
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.local.model.weather.CityAppSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class WeatherSearchManager(private val appContext: Context) {

    private var session: AppSearchSession? = null

    @SuppressLint("NewApi")
    suspend fun init() {
        Timber.i("WeatherSearchManager | init")

        withContext(Dispatchers.IO) {
            val sessionFuture: ListenableFuture<AppSearchSession> =
                if (LabCompatibilityManager.isS()) {
                    Timber.i("WeatherSearchManager | LabCompatibilityManager is Android 12+")

                    PlatformStorage.createSearchSessionAsync(
                        PlatformStorage.SearchContext.Builder(
                            appContext,
                            Constants.APP_SEARCH_WEATHER_CITIES_DATABASE_NAME
                        )
                            .build()
                    )
                } else {
                    Timber.e("WeatherSearchManager | LabCompatibilityManager is below Android 12")

                    LocalStorage.createSearchSessionAsync(
                        LocalStorage.SearchContext.Builder(
                            appContext,
                            Constants.APP_SEARCH_WEATHER_CITIES_DATABASE_NAME
                        )
                            .build()
                    )
                }
            val setSchemaRequest =
                SetSchemaRequest.Builder().addDocumentClasses(CityAppSearch::class.java).build()

            session = sessionFuture.get()
            session?.setSchemaAsync(setSchemaRequest)
        }
    }

    suspend fun putCities(cities: List<CityAppSearch>): Boolean {
        Timber.i("putCities | cities size: ${cities.size}")

        return withContext(Dispatchers.IO) {
            session?.putAsync(
                PutDocumentsRequest.Builder().addDocuments(cities).build()
            )
                ?.get()
                ?.isSuccess == true
        }
    }

    suspend fun searchCities(query: String): List<CityAppSearch> {
        Timber.d("searchCities() | query: $query")

        return withContext(Dispatchers.IO) {
            val searchSpec = SearchSpec.Builder()
                .setSnippetCount(10_000)
                .addFilterNamespaces(Constants.APP_SEARCH_WEATHER_NAMESPACE)
                .setRankingStrategy(SearchSpec.RANKING_STRATEGY_NONE)
//                .setRankingStrategy(SearchSpec.RANKING_STRATEGY_USAGE_COUNT)
                .build()

            val result =
                session?.search(query, searchSpec) ?: return@withContext emptyList<CityAppSearch>()

            val page = result.nextPageAsync.get()

            page.mapNotNull {
                if (CityAppSearch::class.java.simpleName == it.genericDocument.schemaType) {
                    Timber.d("searchCities() | document matches CityAppSearch schema")
                    it.getDocument(CityAppSearch::class.java)
                } else null
            }
        }
    }

    fun closeSession() {
        Timber.e("closeSession()")
        session?.close()
        session = null
    }
}*/

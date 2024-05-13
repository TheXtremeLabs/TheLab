/*
package com.riders.thelab.feature.weather.core.utils

import android.content.Context
import androidx.appsearch.app.AppSearchSession
import androidx.appsearch.app.PutDocumentsRequest
import androidx.appsearch.app.SearchSpec
import androidx.appsearch.app.SetSchemaRequest
import androidx.appsearch.localstorage.LocalStorage
import com.riders.thelab.core.data.local.model.weather.CityAppSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherSearchManager(private val appContext: Context) {

    private var session: AppSearchSession? = null

    suspend fun init() {
        withContext(Dispatchers.IO) {
            val sessionFuture = LocalStorage.createSearchSessionAsync(
                LocalStorage.SearchContext.Builder(
                    appContext, "cities"
                )
                    .build()
            )

            val setSchemaRequest =
                SetSchemaRequest.Builder().addDocumentClasses(CityAppSearch::class.java).build()

            session = sessionFuture.get()
            session?.setSchemaAsync(setSchemaRequest)
        }
    }

    suspend fun putCities(cities: List<CityAppSearch>): Boolean {
        return withContext(Dispatchers.IO) {
            session?.putAsync(
                PutDocumentsRequest.Builder().addDocuments(cities).build()
            )
                ?.get()
                ?.isSuccess == true
        }
    }

    suspend fun searchCities(query: String): List<CityAppSearch> {
        return withContext(Dispatchers.IO) {
            val searchSpec = SearchSpec.Builder()
                .setSnippetCount(10)
                .addFilterNamespaces("weather_search")
                .setRankingStrategy(SearchSpec.RANKING_STRATEGY_USAGE_COUNT)
                .build()

            val result =
                session?.search(query, searchSpec) ?: return@withContext emptyList<CityAppSearch>()

            val page = result.nextPageAsync.get()

            page.mapNotNull {
                if (CityAppSearch::class.java.simpleName == it.genericDocument.schemaType) {
                    it.getDocument(CityAppSearch::class.java)
                } else null
            }
        }
    }

    suspend fun closeSession() {
        session?.close()
        session = null
    }
}*/

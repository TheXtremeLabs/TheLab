package com.riders.thelab.feature.koin.data.remote

import com.riders.thelab.core.data.utils.ErrorType
import com.riders.thelab.core.data.utils.Resource
import com.riders.thelab.core.data.utils.toErrorType
import com.riders.thelab.feature.koin.data.remote.dto.PowerBook
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single
import timber.log.Timber
import java.io.IOException
import kotlin.random.Random

@Single
class ApiImpl() : IApi {

    // Koin
    private val powerBooks: List<PowerBook> = listOf(
        PowerBook("Power Book : Ghost"),
        PowerBook("Power Book : Shawn"),
        PowerBook("Power Book : Tariq"),
        PowerBook("Power Book : Kanan"),
        PowerBook("Power Book : Tommy")
    )


    override suspend fun fetchPowerBooks(): Flow<Resource<List<PowerBook>>> = flow {
        when (Random.nextBoolean()) {
            true -> emit(Resource.Success(powerBooks))
            false -> throw IOException()
        }
    }
        .flowOn(Dispatchers.IO)


    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = true
    }

    private var ktorClient: HttpClient = HttpClient(Android) {

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.v("HTTP Client", null, message)
                }
            }.also { Napier.base(DebugAntilog()) }

            level = LogLevel.HEADERS
            /*filter { request ->
                request.url.host.contains("ktor.io")
            }*/
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }

        install(ContentNegotiation) {
            json(json = json)
        }

        // The retryOnServerErrors function enables retrying a request if a 5xx response
        // is received from a server and specifies the number of retries.
        // exponentialDelay specifies an exponential delay between retries,
        // which is calculated using the Exponential backoff algorithm.
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 5)
            exponentialDelay()
            retryIf { request, response ->
                !response.status.isSuccess()
            }
            retryOnExceptionIf { request, cause ->
                cause.toErrorType() is ErrorType.Api.Network
            }
            delayMillis { retry -> retry * 3000L } // retries in 3, 6, 9, etc. seconds
            // Retry conditions
            modifyRequest { request ->
                request.headers.append(
                    "x-retry-count",
                    retryCount.toString()
                )
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 1000
        }

        engine {
            // this: AndroidEngineConfig
            connectTimeout = 100_000
            socketTimeout = 100_000
            // proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("localhost", 8080))
        }
    }

    override suspend fun fetchGoogle(): Resource<String> {
        Timber.d("fetchGoogle()")

        return this.ktorClient.use { client ->
            client.runCatching {
                val result = this
                    .get("https://www.google.fr/") {
                        Timber.d("fetchGoogle() | client.get | body : $body")
                    }
                    .apply {
                        if (200 != this.status.value && !this.status.isSuccess()) {
                            Timber.e("fetchGoogle() | response code : ${this.status.value}")
                            return@use Resource.ErrorWithType(ErrorType.Api.Network)
                        } else {
                            Timber.v("fetchGoogle() | response code : ${this.status.value}")
                        }
                    }
                    .run { body<String>().toString() }
                // .body<String>()
                // .toString()

                return@use Resource.Success(result)
            }
                .onFailure { exception ->
                    exception.printStackTrace()
                    Timber.e("fetchGoogle() | Error caught with message : ${exception.message} (class : ${exception.javaClass.canonicalName})")
                    return@use Resource.ErrorWithType(exception.toErrorType())
                }
                .onSuccess {
                    Timber.d("fetchGoogle() | Operation successfully executed")
                }
                .getOrElse { Resource.ErrorWithType(it.toErrorType()) }
        }
    }
}
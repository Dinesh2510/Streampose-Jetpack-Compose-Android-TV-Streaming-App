package com.pixeldev.composetv.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import android.content.Context
import android.util.Log
import com.pixeldev.composetv.utlis.Constants.Companion.BASE_URL
import com.pixeldev.composetv.data.remote.ApiService


import dagger.hilt.android.qualifiers.ApplicationContext

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.http.URLProtocol
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

   /* @Provides
    @Singleton
    fun provideApiService(client: HttpClient): ApiService = ApiServiceImpl(client)
*/
   @Provides
   fun provideApiService(client: HttpClient): ApiService = ApiService(client)

    @Provides
    fun provideKtorClient(@ApplicationContext context: Context): HttpClient {
        return HttpClient(provideEngine(context)) {
            expectSuccess = true
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = BASE_URL
                }
            }
            /*install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(accessToken = "token", refreshToken = "")
                    }
                }
            }*/
            install(Logging) {
                /*logger = Logger.ANDROID
                level = LogLevel.BODY*/
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.e("KtorClient","Logger Ktor => $message" )                    }
                }
                level = LogLevel.ALL
                /*filter { request ->
                    request.url.host.contains(BASE_URL)
                }
                sanitizeHeader { header -> header == HttpHeaders.Authorization }*/
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            HttpResponseValidator {
                validateResponse { response ->
                    val statusCode = response.status.value
                    if (statusCode >= 400) {
                        throw ResponseException(response, "HTTP error ${response.status}")
                    }
                }

// Inside your HttpClient config
                handleResponseExceptionWithRequest { cause, _ ->
                    when (cause) {
                        is ClientRequestException -> {
                            // 4xx response
                            Log.e(
                                "KtorClient",
                                "Client error: ${cause.response.status} | ${cause.message}"
                            )
                        }

                        is ServerResponseException -> {
                            // 5xx response
                            Log.e(
                                "KtorClient",
                                "Server error: ${cause.response.status} | ${cause.message}"
                            )
                        }

                        is ResponseException -> {
                            // Non-2xx response not strictly 4xx/5xx
                            Log.e(
                                "KtorClient",
                                "Response error: ${cause.response.status} | ${cause.message}"
                            )
                        }

                        is IOException -> {
                            // Network-related exceptions
                            Log.e("KtorClient", "Network I/O error: ${cause.message}")
                        }

                        else -> {
                            Log.e("KtorClient", "Unexpected error: ${cause.message}")
                        }
                    }
                }

            }

            install(ResponseObserver) {
                onResponse { response ->
                    Log.d("", "HTTP status:" + "${response.status.value}")
                }
            }
        }
    }

    private const val TIME_OUT_MILLIS = 30_000L // 30 seconds

    private fun provideEngine(context: Context): HttpClientEngine {
        return OkHttp.create {
            config {
                connectTimeout(TIME_OUT_MILLIS, TimeUnit.MILLISECONDS)
                readTimeout(TIME_OUT_MILLIS, TimeUnit.MILLISECONDS)
                writeTimeout(TIME_OUT_MILLIS, TimeUnit.MILLISECONDS)

                // Optional: Add interceptors like logging or custom headers
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY // or HEADERS / BASIC
                })

                // You can add your own interceptor here as well
                // addInterceptor(CustomInterceptor())
            }
        }
    }
}
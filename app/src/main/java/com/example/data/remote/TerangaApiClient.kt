package com.example.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class TerangaApiClient(baseUrl: String) {
    private val api: TerangaApi? = baseUrl
        .takeIf { it.startsWith("https://") }
        ?.let { normalizedUrl ->
            Retrofit.Builder()
                .baseUrl(if (normalizedUrl.endsWith('/')) normalizedUrl else "$normalizedUrl/")
                .client(
                    OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .build()
                )
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(TerangaApi::class.java)
        }

    suspend fun checkHealth(): BackendHealth {
        val service = api ?: return BackendHealth.Unconfigured
        return runCatching {
            val response = service.health()
            if (response.isSuccessful && response.body()?.database == "ready") {
                BackendHealth.Ready
            } else {
                BackendHealth.Unavailable
            }
        }.getOrDefault(BackendHealth.Unavailable)
    }
}

enum class BackendHealth {
    Ready,
    Unavailable,
    Unconfigured
}

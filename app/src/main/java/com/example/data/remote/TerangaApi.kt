package com.example.data.remote

import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.GET

@JsonClass(generateAdapter = true)
data class HealthResponse(
    val status: String,
    val database: String? = null
)

interface TerangaApi {
    @GET("api/health")
    suspend fun health(): Response<HealthResponse>
}

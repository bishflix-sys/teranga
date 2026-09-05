package com.example.data.remote

import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

@JsonClass(generateAdapter = true)
data class HealthResponse(
    val status: String,
    val database: String? = null
)

@JsonClass(generateAdapter = true)
data class AuthRequest(
    val email: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class AuthUser(
    val id: String,
    val email: String
)

@JsonClass(generateAdapter = true)
data class AuthResponse(
    val token: String,
    val user: AuthUser
)

interface TerangaApi {
    @GET("api/health")
    suspend fun health(): Response<HealthResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: AuthRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>
}

package com.example.golden_rose_apk.repository

import com.example.golden_rose_apk.config.Constants
import com.example.golden_rose_apk.model.AuthResponse
import com.example.golden_rose_apk.model.LoginRequest
import com.example.golden_rose_apk.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
}

class AuthRepository {
    private val api: AuthApi = RetrofitProvider.build(Constants.BASE_AUTH, AuthApi::class.java)

    suspend fun login(email: String, password: String): AuthResponse =
        api.login(LoginRequest(email = email, password = password))

    suspend fun register(username: String, email: String, password: String): AuthResponse =
        api.register(RegisterRequest(nombre = username, email = email, password = password))
}
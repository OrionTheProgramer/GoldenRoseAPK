package com.example.golden_rose_apk.service

import com.example.golden_rose_apk.model.AuthResponse
import com.example.golden_rose_apk.model.LoginRequest
import com.example.golden_rose_apk.model.LoginResponse
import com.example.golden_rose_apk.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<LoginResponse>
}

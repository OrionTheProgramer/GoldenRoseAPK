package com.example.golden_rose_apk.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("GoldenRosePrefs", Context.MODE_PRIVATE)

    private val _isLoggedIn = MutableStateFlow<Boolean>(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole

    private val _authToken = MutableStateFlow<String?>(null)
    val authToken: StateFlow<String?> = _authToken

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId


    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        val role = sharedPreferences.getString("USER_ROLE", null)
        _isLoggedIn.value = role != null
        _userRole.value = role
    }

    fun login(role: String, token: String, username: String? = null) {
        viewModelScope.launch {
            with(sharedPreferences.edit()) {
                putString("USER_ROLE", role)
                putString("AUTH_TOKEN", token)
                username?.let { putString("USER_NAME", it) }
                apply()
            }
            _isLoggedIn.value = true
            _userRole.value = role
        }
    }

    fun logout() {
        viewModelScope.launch {
            with(sharedPreferences.edit()) {
                remove("USER_ROLE")
                remove("AUTH_TOKEN")
                remove("USER_NAME")
                apply()
            }
            _isLoggedIn.value = false
            _userRole.value = null
        }
    }
}

class AuthViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
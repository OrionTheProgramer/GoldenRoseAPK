package com.example.golden_rose_apk.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("GoldenRosePrefs", Context.MODE_PRIVATE)

    private val _username = MutableStateFlow<String>("")
    val username: StateFlow<String> = _username

    private val _receiveOffers = MutableStateFlow<Boolean>(false)
    val receiveOffers: StateFlow<Boolean> = _receiveOffers

    private val _appTheme = MutableStateFlow<String>("dark")
    val appTheme: StateFlow<String> = _appTheme

    private val _pushNotificationsEnabled = MutableStateFlow<Boolean>(false)
    val pushNotificationsEnabled: StateFlow<Boolean> = _pushNotificationsEnabled

    init {
        loadSettings()
    }

    private fun loadSettings() {
        _username.value = sharedPreferences.getString("USER_NAME", "Usuario Golden") ?: "Usuario Golden"
        _receiveOffers.value = sharedPreferences.getBoolean("RECEIVE_OFFERS", false)
        _appTheme.value = sharedPreferences.getString("APP_THEME", "dark") ?: "dark"
        _pushNotificationsEnabled.value = sharedPreferences.getBoolean("PUSH_NOTIFICATIONS", false)
    }

    fun updateUsername(newName: String) {
        _username.value = newName
        sharedPreferences.edit().putString("USER_NAME", newName).apply()
    }

    fun setReceiveOffers(enabled: Boolean) {
        _receiveOffers.value = enabled
        sharedPreferences.edit().putBoolean("RECEIVE_OFFERS", enabled).apply()
    }

    fun setAppTheme(theme: String) {
        _appTheme.value = theme
        sharedPreferences.edit().putString("APP_THEME", theme).apply()
    }

    fun setPushNotificationsEnabled(enabled: Boolean) {
        _pushNotificationsEnabled.value = enabled
        sharedPreferences.edit().putBoolean("PUSH_NOTIFICATIONS", enabled).apply()
    }

    fun setTheme(theme: String) {
        setAppTheme(theme)
    }
}

class SettingsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

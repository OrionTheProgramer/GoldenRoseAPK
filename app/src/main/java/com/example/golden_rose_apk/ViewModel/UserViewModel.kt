package com.example.golden_rose_apk.ViewModel

import androidx.lifecycle.ViewModel
import com.example.golden_rose_apk.model.UserUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class UserViewModel : ViewModel() {

    private val _userUiState = MutableStateFlow(UserUi())
    val userUiState: StateFlow<UserUi> = _userUiState

    fun onDisplayNameChange(value: String) {
        _userUiState.update { it.copy(displayName = value) }
    }

    fun onNicknameChange(value: String) {
        _userUiState.update { it.copy(nickname = value) }
    }

    fun onCountryChange(value: String) {
        _userUiState.update { it.copy(country = value) }
    }

    fun onDarkModeChange(value: Boolean) {
        _userUiState.update { it.copy(darkMode = value) }
        // aquí podrías llamar a algo que guarde en DataStore / Firebase
    }

    suspend fun saveProfile(): Boolean {
        return try {
            // TODO: guardar en Firebase / backend
            true
        } catch (e: Exception) {
            false
        }
    }
}

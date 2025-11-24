package com.example.ticketway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketway.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class RegistrationStep {
    AUTH_COMPLETE, // Auth successful, proceed to profile setup
    NONE // Initial or failed state
}

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    // authState: true for final success (ready for home), false for failure, null for initial/logged out
    private val _authState = MutableStateFlow<Boolean?>(null)
    val authState = _authState.asStateFlow()

    // registrationState: used only for multi-step registration flow
    private val _registrationState = MutableStateFlow<RegistrationStep>(RegistrationStep.NONE)
    val registrationState = _registrationState.asStateFlow()

    // errorMessage: specific error message to be displayed in the UI
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _errorMessage.value = null
            val result = repository.register(email, password)
            if (result.isSuccess) {
                // Do NOT set authState to true yet, user must complete profile setup first
                _registrationState.value = RegistrationStep.AUTH_COMPLETE
            } else {
                _authState.value = false
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun updateUserProfile(name: String, phone: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = repository.updateUserProfile(name, phone)
            if (result.isSuccess) {
                // Profile setup is complete, now user is fully authenticated and ready for home screen
                _authState.value = true
                _registrationState.value = RegistrationStep.NONE
                onComplete(true)
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message
                onComplete(false)
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _errorMessage.value = null
            val result = repository.login(email, password)
            if (result.isSuccess) {
                _authState.value = true
            } else {
                _authState.value = false
                _errorMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun logout() {
        repository.logout()
        _authState.value = false
    }

    fun checkUser() {
        _authState.value = repository.currentUser != null
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
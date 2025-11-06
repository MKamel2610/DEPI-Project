package com.example.ticketway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketway.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _authState = MutableStateFlow<Boolean?>(null)
    val authState = _authState.asStateFlow()

    fun register(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.register(email, password)
            _authState.value = result.isSuccess
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.login(email, password)
            _authState.value = result.isSuccess
        }
    }

    fun logout() {
        repository.logout()
        _authState.value = false
    }

    fun checkUser() {
        _authState.value = repository.currentUser != null
    }
}

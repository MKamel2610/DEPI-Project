package com.example.ticketway.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketway.data.model.UserProfile
import com.example.ticketway.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val repo: UserRepository = UserRepository()
) : ViewModel() {

    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile

    fun loadUser() {
        viewModelScope.launch {
            _profile.value = repo.getUserProfile()
        }
    }

    fun saveUser(profile: UserProfile, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            repo.saveUserProfile(profile)
            onSuccess()
        }
    }
}

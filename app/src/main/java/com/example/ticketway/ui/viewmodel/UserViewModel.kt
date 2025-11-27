package com.example.ticketway.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketway.data.model.UserProfile
import com.example.ticketway.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject // NEW IMPORT
import dagger.hilt.android.lifecycle.HiltViewModel // NEW IMPORT

// This ViewModel handles fetching and updating the *details* of the authenticated user.
@HiltViewModel // NEW ANNOTATION
class UserViewModel @Inject constructor( // NEW ANNOTATION
    private val repo: UserRepository // Hilt will inject UserRepository
) : ViewModel() {

    // Holds the fetched profile data (or null if not loaded/error)
    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        // Automatically load profile when ViewModel is created (e.g., when screen is navigated to)
        loadUser()
    }

    fun loadUser() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _profile.value = repo.getUserProfile()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load profile: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Renamed from saveUser to updateUserProfile for clarity in the UI context
    fun updateUserProfile(updatedProfile: UserProfile, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                // The UserProfile object from Firestore does not contain the UID,
                // but the UserRepository uses the current FirebaseAuth user's UID to save.
                repo.saveUserProfile(updatedProfile)
                _profile.value = updatedProfile // Optimistically update the UI state
                onComplete(true)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update profile: ${e.message}"
                onComplete(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
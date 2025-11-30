package com.example.ticketway.ui.screens

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalFocusManager
import com.example.ticketway.ui.viewmodel.AuthViewModel
// Import the stateless content
import com.example.ticketway.ui.screens.previews.ProfileSetupContent as StatelessProfileSetupContent

@Composable
fun ProfileSetupScreen(
    viewModel: AuthViewModel,
    onSetupComplete: () -> Unit
) {
    // 1. Local UI State (Hoisted State)
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current

    // 2. Derived State for Button Enablement
    val isFormValid = remember(firstName, lastName, phone) {
        val fullName = "$firstName $lastName".trim()
        val digitsOnlyPhone = phone.filter { it.isDigit() }

        fullName.isNotBlank() && digitsOnlyPhone.length >= 7
    }

    val isButtonEnabled = isFormValid && !isLoading

    // 3. Action Logic (Validation + ViewModel Call)
    val onComplete: () -> Unit = {
        focusManager.clearFocus()
        val fullName = "$firstName $lastName".trim()
        val digitsOnlyPhone = phone.filter { it.isDigit() }

        // --- FIX: Replace return@onComplete with conditional logic ---
        // Local Validation
        if (fullName.isBlank() || phone.isBlank()) {
            errorMessage = "Please enter your full name and phone number."
        } else if (digitsOnlyPhone.length < 7) {
            errorMessage = "Please enter a valid phone number (at least 7 digits)."
        } else {
            // If validation passes, proceed to API call
            errorMessage = null
            isLoading = true

            viewModel.updateUserProfile(fullName, phone) { success ->
                isLoading = false
                if (success) {
                    onSetupComplete()
                } else {
                    errorMessage = "Failed to save profile. Please try again."
                }
            }
        }
    }

    // 4. Input Handler Functions (Set state and clear error)
    val onFirstNameChange: (String) -> Unit = {
        firstName = it
        errorMessage = null
    }

    val onLastNameChange: (String) -> Unit = {
        lastName = it
        errorMessage = null
    }

    val onPhoneChange: (String) -> Unit = {
        // Original code filtered out non-digits; we will keep that logic here
        phone = it.filter { char -> char.isDigit() || char == '+' }
        errorMessage = null
    }

    // 5. Call Stateless Content
    StatelessProfileSetupContent(
        firstName = firstName,
        lastName = lastName,
        phone = phone,
        isLoading = isLoading,
        errorMessage = errorMessage,
        isButtonEnabled = isButtonEnabled,
        onFirstNameChange = onFirstNameChange,
        onLastNameChange = onLastNameChange,
        onPhoneChange = onPhoneChange,
        onComplete = onComplete
    )
}
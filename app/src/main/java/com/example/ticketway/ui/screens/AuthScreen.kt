package com.example.ticketway.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.ticketway.ui.viewmodel.AuthViewModel
import com.example.ticketway.ui.viewmodel.RegistrationStep
import com.example.ticketway.ui.ui.theme.*
import com.example.ticketway.ui.screens.previews.*


@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onAuthSuccess: () -> Unit = {},
    onRegistrationSuccess: () -> Unit = {},
    onSkip: (() -> Unit)? = null,
    showSkipButton: Boolean = true
) {
    // ViewModel state
    val authState by viewModel.authState.collectAsState()
    val registrationState by viewModel.registrationState.collectAsState()
    val vmErrorMessage by viewModel.errorMessage.collectAsState()

    // Local UI state
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var localError by remember { mutableStateOf<String?>(null) }
    val errorMessage = vmErrorMessage ?: localError

    // Handle ViewModel login success
    LaunchedEffect(authState) {
        if (authState == true) {
            isLoading = false
            localError = null
            onAuthSuccess()
        } else if (authState == false) {
            isLoading = false
        }
    }

    // Handle Firebase registration
    LaunchedEffect(registrationState) {
        if (registrationState == RegistrationStep.AUTH_COMPLETE) {
            isLoading = false
            localError = null
            onRegistrationSuccess()
        }
    }

    // Helper validator
    fun validateAndRunAuth(loginMode: Boolean) {
        val error = validateAuth(
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            isLogin = loginMode
        )

        if (error != null) {
            localError = error
            return
        }

        // All validations passed
        localError = null
        viewModel.clearErrorMessage()
        isLoading = true

        if (loginMode) {
            viewModel.login(email, password)
        } else {
            viewModel.register(email, password)
        }
    }

    // Render UI
    AuthScreenContent(
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        isLogin = isLogin,
        passwordVisible = passwordVisible,
        isLoading = isLoading,
        errorMessage = errorMessage,
        showSkipButton = showSkipButton,
        onEmailChange = {
            email = it.trim()
            localError = null
            viewModel.clearErrorMessage()
        },
        onPasswordChange = {
            password = it
            localError = null
            viewModel.clearErrorMessage()
        },
        onConfirmPasswordChange = {
            confirmPassword = it
            localError = null
            viewModel.clearErrorMessage()
        },
        onPasswordVisibilityToggle = {
            passwordVisible = !passwordVisible
        },
        onAuthAction = {
            validateAndRunAuth(isLogin)
        },
        onToggleMode = {
            isLogin = !isLogin
            confirmPassword = ""
            localError = null
            viewModel.clearErrorMessage()
        },
        onSkip = { onSkip?.invoke() ?: Unit }
    )
}

private fun validateAuth(
    email: String,
    password: String,
    confirmPassword: String,
    isLogin: Boolean
): String? {
    if (email.isBlank() || password.isBlank()) {
        return "Please fill in all fields"
    }

    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        return "Please enter a valid email address"
    }

    if (password.length < 6) {
        return "Password must be at least 6 characters"
    }

    if (!isLogin && password != confirmPassword) {
        return "Passwords do not match"
    }

    return null
}

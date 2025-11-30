package com.example.ticketway.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.ticketway.data.model.UserProfile
import com.example.ticketway.ui.user.UserViewModel
// Import the stateless content
import com.example.ticketway.ui.screens.profile.previews.ProfileScreenContent as StatelessProfileScreenContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: UserViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    // 1. ViewModel State Collection
    val profileState by viewModel.profile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // 2. Local Editable UI State
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    // 3. Side Effects/Data Synchronization
    LaunchedEffect(Unit) {
        if (profileState == null) {
            viewModel.loadUser()
        }
    }

    LaunchedEffect(profileState) {
        profileState?.let {
            name = it.name
            phone = it.phone
        }
    }

    // 4. Derived State and Logic

    val showLoadingScreen = isLoading && profileState == null

    val isSaveButtonEnabled = !isLoading && name.isNotBlank() && phone.isNotBlank()

    // Logic for saving the profile
    val onSave: () -> Unit = {
        focusManager.clearFocus()
        if (profileState != null) {
            viewModel.updateUserProfile(
                updatedProfile = UserProfile(
                    email = profileState!!.email,
                    name = name.trim(),
                    phone = phone.trim()
                ),
                onComplete = { success -> if (success) onBack() }
            )
        }
    }

    // Input handlers with validation/filtering
    val onNameChange: (String) -> Unit = { name = it }
    val onPhoneChange: (String) -> Unit = { phone = it.filter { it.isDigit() || it == '+' } }

    // 5. Render UI based on State
    if (showLoadingScreen) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else if (profileState != null) {
        StatelessProfileScreenContent(
            email = profileState!!.email,
            name = name,
            phone = phone,
            isLoading = isLoading,
            errorMessage = errorMessage,
            onNameChange = onNameChange,
            onPhoneChange = onPhoneChange,
            onSave = onSave,
            onLogout = onLogout,
            isSaveButtonEnabled = isSaveButtonEnabled
        )
    } else {
        // Error state when profile fails to load initially
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = errorMessage ?: "Could not load profile data.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(onClick = { viewModel.loadUser() }) {
                    Text("Retry")
                }
            }
        }
    }
}
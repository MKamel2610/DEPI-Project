package com.example.ticketway.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketway.data.model.UserProfile
import com.example.ticketway.ui.user.UserViewModel
import com.example.ticketway.ui.ui.theme.PrimaryGreen
import com.example.ticketway.ui.ui.theme.DarkText
import com.example.ticketway.ui.ui.theme.LightText
import com.example.ticketway.ui.ui.theme.LightGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: UserViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val profileState by viewModel.profile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // State for editable fields (hoisted from ViewModel data)
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    // FIX 1: Explicitly ensure data loads every time the screen is accessed if data is missing.
    LaunchedEffect(Unit) {
        if (profileState == null) {
            viewModel.loadUser()
        }
    }

    // Update local state when profile data loads or changes
    LaunchedEffect(profileState) {
        profileState?.let {
            name = it.name
            phone = it.phone
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Profile", color = DarkText, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go back", tint = DarkText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LightGray)
            )
        },
        containerColor = LightGray
    ) { padding ->

        // Check if we are currently loading AND have no data
        val showLoading = isLoading && profileState == null

        if (showLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else if (profileState != null) {
            // --- MAIN CONTENT ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // --- Profile Icon / Avatar Placeholder ---
                Icon(
                    Icons.Default.Person,
                    contentDescription = "User Avatar",
                    modifier = Modifier.size(96.dp),
                    tint = PrimaryGreen
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- Email (Read-only) ---
                OutlinedTextField(
                    value = profileState!!.email,
                    onValueChange = { /* Read-only */ },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email", tint = LightText) },
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --- Name (Editable) ---
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Full Name", tint = PrimaryGreen) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --- Phone (Editable) ---
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it.filter { it.isDigit() || it == '+' } },
                    label = { Text("Phone Number") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone", tint = PrimaryGreen) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    enabled = !isLoading
                )

                // --- Error Message ---
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- Save Button ---
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        if (name.isBlank() || phone.isBlank()) {
                            viewModel.clearErrorMessage()
                            viewModel.updateUserProfile(
                                updatedProfile = UserProfile(
                                    email = profileState!!.email,
                                    name = name.trim(),
                                    phone = phone.trim()
                                ),
                                onComplete = { success ->
                                    if (success) onBack()
                                }
                            )
                        } else {
                            viewModel.updateUserProfile(
                                updatedProfile = UserProfile(
                                    email = profileState!!.email,
                                    name = name.trim(),
                                    phone = phone.trim()
                                ),
                                onComplete = { success ->
                                    if (success) onBack()
                                }
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Text(text = "Save Profile", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- Logout Button ---
                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, LightText),
                    enabled = !isLoading
                ) {
                    Text(text = "Logout", fontSize = 16.sp, color = LightText, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        } else {
            // FIX 2: Show an error/retry button if loading finished but profile is still null (blank screen prevention)
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
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
}
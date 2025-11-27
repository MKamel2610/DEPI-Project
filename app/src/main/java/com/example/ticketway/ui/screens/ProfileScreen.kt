package com.example.ticketway.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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

    // Relying on parent Scaffold for padding and white background
    val showLoading = isLoading && profileState == null

    if (showLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PrimaryGreen)
        }
    } else if (profileState != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp), // Added top/bottom padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Header/Avatar ---
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(PrimaryGreen.copy(alpha = 0.1f), RoundedCornerShape(60.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "User Avatar",
                    modifier = Modifier.size(80.dp),
                    tint = PrimaryGreen
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title for context (Removed TopAppBar so adding title here)
            Text(
                text = "My Profile",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- Profile Card/Fields ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // --- Email (Read-only) ---
                    ProfileTextField(
                        value = profileState!!.email,
                        label = "Email",
                        icon = Icons.Default.Email,
                        readOnly = true,
                        iconTint = LightText
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // --- Name (Editable) ---
                    ProfileTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Full Name",
                        icon = Icons.Default.Person,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        enabled = !isLoading
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // --- Phone (Editable) ---
                    ProfileTextField(
                        value = phone,
                        onValueChange = { phone = it.filter { it.isDigit() || it == '+' } },
                        label = "Phone Number",
                        icon = Icons.Default.Phone,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        enabled = !isLoading
                    )
                }
            }


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
                    // Simplified logic: If we have profile data, attempt update
                    if (profileState != null) {
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
                enabled = !isLoading && name.isNotBlank() && phone.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text(text = "Save Profile", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

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
        // Show an error/retry button if loading finished but profile is still null
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

// Helper Composable for clean TextField design
@Composable
fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit = {},
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    iconTint: Color = PrimaryGreen
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = label, tint = iconTint) },
        readOnly = readOnly,
        enabled = enabled,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryGreen,
            unfocusedBorderColor = LightText.copy(alpha = 0.5f),
            disabledBorderColor = LightText.copy(alpha = 0.5f)
        )
    )
}
package com.example.ticketway.ui.screens.previews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ==================== Preview-Friendly Composable ====================

@Composable
fun AuthScreenContent(
    email: String,
    password: String,
    confirmPassword: String,
    isLogin: Boolean,
    passwordVisible: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    showSkipButton: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onAuthAction: () -> Unit,
    onToggleMode: () -> Unit,
    onSkip: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App branding
            Text(
                text = "⚽",
                fontSize = 72.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "TicketWay",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = if (isLogin) "Welcome back!" else "Create your account",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 40.dp)
            )

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Email") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = "Email",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    autoCorrectEnabled = false
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                enabled = !isLoading,
                isError = errorMessage != null
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Password") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    IconButton(onClick = onPasswordVisibilityToggle) {
                        Text(
                            text = if (passwordVisible) "👁" else "👁‍🗨",
                            fontSize = 20.sp
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = if (isLogin) ImeAction.Done else ImeAction.Next
                ),
                enabled = !isLoading,
                isError = errorMessage != null
            )

            // Confirm password field (only for sign up)
            if (!isLogin) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = { Text("Confirm Password") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Confirm Password",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    enabled = !isLoading,
                    isError = errorMessage != null
                )
            }

            // Error message
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Main action button
            Button(
                onClick = onAuthAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (isLogin) "Login" else "Sign Up",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Toggle between login and register
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isLogin) "Don't have an account?" else "Already have an account?",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(
                    onClick = onToggleMode,
                    enabled = !isLoading
                ) {
                    Text(
                        text = if (isLogin) "Sign Up" else "Login",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Skip button
            if (showSkipButton) {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = onSkip,
                    enabled = !isLoading
                ) {
                    Text(
                        text = "Skip for now",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

// ==================== Previews ====================

@Preview(name = "Login Screen - Empty", showBackground = true)
@Composable
private fun PreviewAuthScreenLoginEmpty() {
    MaterialTheme {
        AuthScreenContent(
            email = "",
            password = "",
            confirmPassword = "",
            isLogin = true,
            passwordVisible = false,
            isLoading = false,
            errorMessage = null,
            showSkipButton = true,
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onPasswordVisibilityToggle = {},
            onAuthAction = {},
            onToggleMode = {},
            onSkip = {}
        )
    }
}

@Preview(name = "Login Screen - Filled", showBackground = true)
@Composable
private fun PreviewAuthScreenLoginFilled() {
    MaterialTheme {
        AuthScreenContent(
            email = "user@example.com",
            password = "password123",
            confirmPassword = "",
            isLogin = true,
            passwordVisible = false,
            isLoading = false,
            errorMessage = null,
            showSkipButton = true,
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onPasswordVisibilityToggle = {},
            onAuthAction = {},
            onToggleMode = {},
            onSkip = {}
        )
    }
}

@Preview(name = "Login Screen - With Error", showBackground = true)
@Composable
private fun PreviewAuthScreenLoginError() {
    MaterialTheme {
        AuthScreenContent(
            email = "user@example.com",
            password = "wrong",
            confirmPassword = "",
            isLogin = true,
            passwordVisible = false,
            isLoading = false,
            errorMessage = "Invalid email or password",
            showSkipButton = true,
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onPasswordVisibilityToggle = {},
            onAuthAction = {},
            onToggleMode = {},
            onSkip = {}
        )
    }
}

@Preview(name = "Login Screen - Loading", showBackground = true)
@Composable
private fun PreviewAuthScreenLoginLoading() {
    MaterialTheme {
        AuthScreenContent(
            email = "user@example.com",
            password = "password123",
            confirmPassword = "",
            isLogin = true,
            passwordVisible = false,
            isLoading = true,
            errorMessage = null,
            showSkipButton = true,
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onPasswordVisibilityToggle = {},
            onAuthAction = {},
            onToggleMode = {},
            onSkip = {}
        )
    }
}

@Preview(name = "Sign Up Screen - Empty", showBackground = true)
@Composable
private fun PreviewAuthScreenSignUpEmpty() {
    MaterialTheme {
        AuthScreenContent(
            email = "",
            password = "",
            confirmPassword = "",
            isLogin = false,
            passwordVisible = false,
            isLoading = false,
            errorMessage = null,
            showSkipButton = true,
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onPasswordVisibilityToggle = {},
            onAuthAction = {},
            onToggleMode = {},
            onSkip = {}
        )
    }
}

@Preview(name = "Sign Up Screen - Filled", showBackground = true)
@Composable
private fun PreviewAuthScreenSignUpFilled() {
    MaterialTheme {
        AuthScreenContent(
            email = "newuser@example.com",
            password = "password123",
            confirmPassword = "password123",
            isLogin = false,
            passwordVisible = false,
            isLoading = false,
            errorMessage = null,
            showSkipButton = true,
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onPasswordVisibilityToggle = {},
            onAuthAction = {},
            onToggleMode = {},
            onSkip = {}
        )
    }
}

@Preview(name = "Sign Up Screen - Password Mismatch", showBackground = true)
@Composable
private fun PreviewAuthScreenSignUpPasswordMismatch() {
    MaterialTheme {
        AuthScreenContent(
            email = "newuser@example.com",
            password = "password123",
            confirmPassword = "password456",
            isLogin = false,
            passwordVisible = false,
            isLoading = false,
            errorMessage = "Passwords do not match",
            showSkipButton = true,
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onPasswordVisibilityToggle = {},
            onAuthAction = {},
            onToggleMode = {},
            onSkip = {}
        )
    }
}

@Preview(name = "Sign Up Screen - Password Visible", showBackground = true)
@Composable
private fun PreviewAuthScreenSignUpPasswordVisible() {
    MaterialTheme {
        AuthScreenContent(
            email = "newuser@example.com",
            password = "password123",
            confirmPassword = "password123",
            isLogin = false,
            passwordVisible = true,
            isLoading = false,
            errorMessage = null,
            showSkipButton = true,
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onPasswordVisibilityToggle = {},
            onAuthAction = {},
            onToggleMode = {},
            onSkip = {}
        )
    }
}

@Preview(name = "Auth Screen - No Skip Button", showBackground = true)
@Composable
private fun PreviewAuthScreenNoSkip() {
    MaterialTheme {
        AuthScreenContent(
            email = "",
            password = "",
            confirmPassword = "",
            isLogin = true,
            passwordVisible = false,
            isLoading = false,
            errorMessage = null,
            showSkipButton = false,
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onPasswordVisibilityToggle = {},
            onAuthAction = {},
            onToggleMode = {},
            onSkip = {}
        )
    }
}
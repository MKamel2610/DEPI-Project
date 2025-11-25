package com.example.ticketway.ui.screens

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketway.ui.viewmodel.AuthViewModel
import com.example.ticketway.ui.viewmodel.RegistrationStep
import com.example.ticketway.ui.ui.theme.*


@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onAuthSuccess: () -> Unit = {},
    onRegistrationSuccess: () -> Unit = {}, // NEW: Called after successful Firebase Auth registration
    onSkip: (() -> Unit)? = null,
    showSkipButton: Boolean = true
) {
    val authState by viewModel.authState.collectAsState()
    val registrationState by viewModel.registrationState.collectAsState() // NEW: Observe registration step
    val vmErrorMessage by viewModel.errorMessage.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var localErrorMessage by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current

    val currentErrorMessage = vmErrorMessage ?: localErrorMessage

    // Handle Auth state changes
    LaunchedEffect(authState) {
        when (authState) {
            true -> {
                isLoading = false
                localErrorMessage = null
                onAuthSuccess() // Only called on Login or completed registration profile setup
            }
            false -> {
                isLoading = false
            }
            null -> {
                isLoading = false
            }
        }
    }

    // NEW: Handle successful Firebase Auth registration -> move to profile setup
    LaunchedEffect(registrationState) {
        if (registrationState == RegistrationStep.AUTH_COMPLETE) {
            isLoading = false
            onRegistrationSuccess()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
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
                color = Color(0xFF212121)
            )

            Text(
                text = if (isLogin) "Welcome back!" else "Create your account",
                fontSize = 16.sp,
                color = Color(0xFF757575),
                modifier = Modifier.padding(top = 8.dp, bottom = 40.dp)
            )

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it.trim()
                    localErrorMessage = null
                    viewModel.clearErrorMessage()
                },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = "Email",
                        tint = PrimaryGreen
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryGreen,
                    focusedLabelColor = PrimaryGreen
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    autoCorrect = false
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                enabled = !isLoading,
                isError = currentErrorMessage != null
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    localErrorMessage = null
                    viewModel.clearErrorMessage()
                },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = PrimaryGreen
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(
                            text = if (passwordVisible) "👁️" else "👁️‍🗨️",
                            fontSize = 20.sp
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryGreen,
                    focusedLabelColor = PrimaryGreen
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = if (isLogin) ImeAction.Done else ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                    onDone = {
                        if (isLogin) {
                            focusManager.clearFocus()
                            handleAuth(
                                isLogin = true,
                                email = email,
                                password = password,
                                confirmPassword = "",
                                onError = { localErrorMessage = it },
                                onStart = { isLoading = true },
                                onAuth = { viewModel.login(email, password) }
                            )
                        } else {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    }
                ),
                enabled = !isLoading,
                isError = currentErrorMessage != null
            )

            // Confirm password field (only for sign up)
            if (!isLogin) {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        localErrorMessage = null
                        viewModel.clearErrorMessage()
                    },
                    label = { Text("Confirm Password") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Confirm Password",
                            tint = PrimaryGreen
                        )
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        focusedLabelColor = PrimaryGreen
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            handleAuth(
                                isLogin = false,
                                email = email,
                                password = password,
                                confirmPassword = confirmPassword,
                                onError = { localErrorMessage = it },
                                onStart = { isLoading = true },
                                onAuth = { viewModel.register(email, password) }
                            )
                        }
                    ),
                    enabled = !isLoading,
                    isError = currentErrorMessage != null
                )
            }

            // Error message
            if (currentErrorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = currentErrorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Main action button
            Button(
                onClick = {
                    focusManager.clearFocus()
                    handleAuth(
                        isLogin = isLogin,
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword,
                        onError = { localErrorMessage = it },
                        onStart = { isLoading = true },
                        onAuth = {
                            if (isLogin) {
                                viewModel.login(email, password)
                            } else {
                                // Calls viewModel.register, which triggers the registrationState change
                                viewModel.register(email, password)
                            }
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen
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
                        fontWeight = FontWeight.Bold
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
                    color = Color(0xFF757575)
                )
                TextButton(
                    onClick = {
                        isLogin = !isLogin
                        localErrorMessage = null
                        viewModel.clearErrorMessage()
                        confirmPassword = ""
                    },
                    enabled = !isLoading
                ) {
                    Text(
                        text = if (isLogin) "Sign Up" else "Login",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryGreen
                    )
                }
            }

            // Skip button - under the fields
            if (showSkipButton && onSkip != null) {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = onSkip,
                    enabled = !isLoading
                ) {
                    Text(
                        text = "Skip for now",
                        color = Color(0xFF757575),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

private fun handleAuth(
    isLogin: Boolean,
    email: String,
    password: String,
    confirmPassword: String,
    onError: (String) -> Unit,
    onStart: () -> Unit,
    onAuth: () -> Unit
) {
    // Validation
    if (email.isBlank() || password.isBlank()) {
        onError("Please fill in all fields")
        return
    }

    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        onError("Please enter a valid email address")
        return
    }

    if (password.length < 6) {
        onError("Password must be at least 6 characters")
        return
    }

    if (!isLogin && password != confirmPassword) {
        onError("Passwords do not match")
        return
    }

    // All validations passed
    onStart()
    onAuth()
}
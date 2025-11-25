package com.example.ticketway

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.ticketway.data.local.DatabaseProvider
import com.example.ticketway.ui.screens.AuthScreen
import com.example.ticketway.ui.screens.BookingHomeScreen
import com.example.ticketway.ui.screens.ProfileSetupScreen
import com.example.ticketway.ui.screens.profile.ProfileScreen // NEW Import
import com.example.ticketway.ui.theme.TicketWayTheme
import com.example.ticketway.ui.user.UserViewModel // NEW Import
import com.example.ticketway.ui.viewmodel.AuthViewModel
import com.example.ticketway.ui.viewmodel.FixturesViewModel
import com.example.ticketway.ui.viewmodel.FixturesViewModelFactory
import com.example.ticketway.ui.viewmodel.RegistrationStep

class MainActivity : ComponentActivity() {

    private lateinit var fixturesViewModel: FixturesViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var userViewModel: UserViewModel // NEW: For ProfileScreen data

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "onCreate called")

        // Initialize ViewModels
        val db = DatabaseProvider.getDatabase(this)
        val fixturesFactory = FixturesViewModelFactory(db)
        fixturesViewModel = ViewModelProvider(this, fixturesFactory)[FixturesViewModel::class.java]
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java] // NEW: Initialize UserViewModel

        setContent {
            TicketWayTheme {
                AppContent(
                    fixturesViewModel = fixturesViewModel,
                    authViewModel = authViewModel,
                    userViewModel = userViewModel // NEW: Pass UserViewModel
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent(
    fixturesViewModel: FixturesViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel // NEW Parameter
) {
    // Primary navigation states
    var showAuth by remember { mutableStateOf(true) }
    var showAuthModal by remember { mutableStateOf(false) }
    var showProfileSetup by remember { mutableStateOf(false) }
    var currentTab by remember { mutableStateOf("home") } // NEW: Tracks the selected tab

    val authState by authViewModel.authState.collectAsState()
    val registrationState by authViewModel.registrationState.collectAsState()

    // Initialize: check if user is already logged in.
    LaunchedEffect(Unit) {
        authViewModel.checkUser()
    }

    // Update when auth state changes (FINAL success/failure)
    LaunchedEffect(authState) {
        if (authState == true) {
            showAuth = false
            showAuthModal = false
            showProfileSetup = false
            userViewModel.loadUser() // Load profile data upon successful authentication
        } else if (authState == false) {
            // User is explicitly logged out or failed login. Reset to show auth screen
            showAuth = true
            currentTab = "home" // Reset tab selection on logout
        }
    }

    // Handle transition from AuthScreen to ProfileSetupScreen
    LaunchedEffect(registrationState) {
        if (registrationState == RegistrationStep.AUTH_COMPLETE) {
            showAuth = false
            showAuthModal = false
            showProfileSetup = true
        }
    }

    // --- Logout Functionality ---
    val onLogout: () -> Unit = {
        authViewModel.logout()
        // authState = false in LaunchedEffect handles the reset
    }

    // --- Screen Logic ---
    if (showAuth) {
        // Full-screen Auth flow (initial launch or after explicit logout)
        AuthScreen(
            viewModel = authViewModel,
            onAuthSuccess = { /* authState handles showAuth = false */ },
            onRegistrationSuccess = { /* registrationState handles navigation to Profile Setup */ },
            onSkip = { showAuth = false },
            showSkipButton = true
        )
    } else if (showProfileSetup) {
        // Full-screen Profile Setup flow (after registration)
        ProfileSetupScreen(
            viewModel = authViewModel,
            onSetupComplete = { /* authState handles navigation to Home */ }
        )
    } else {
        // Main Home Screen flow with Bottom Navigation
        when (currentTab) {
            "home", "My Tickets" -> {
                // Show Home or My Tickets content (currently only showing BookingHomeScreen)
                BookingHomeScreen(
                    viewModel = fixturesViewModel,
                    currentTab = currentTab, // Pass current tab to Home
                    onTabSelected = { newTab ->
                        currentTab = newTab
                        // If user selects 'account', check auth status
                        if (newTab == "account") {
                            if (authState != true) {
                                showAuthModal = true
                                currentTab = "home" // Stay on home if auth modal is triggered
                            }
                        }
                    },
                    onMatchClick = { fixture ->
                        if (authState != true) {
                            showAuthModal = true
                        } else {
                            // TODO: Go to booking screen
                        }
                    }
                )
            }
            "account" -> {
                // Show Profile Screen
                ProfileScreen(
                    viewModel = userViewModel,
                    onBack = { currentTab = "home" }, // Return to home tab
                    onLogout = onLogout
                )
            }
        }

        // Auth modal (for booking or accessing profile when not logged in)
        if (showAuthModal) {
            ModalBottomSheet(
                onDismissRequest = { showAuthModal = false }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(text = if (currentTab == "account") "Login Required" else "Booking Requires Login", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Please login to continue", style = MaterialTheme.typography.bodyMedium)
                }

                AuthScreen(
                    viewModel = authViewModel,
                    onAuthSuccess = { showAuthModal = false },
                    onRegistrationSuccess = {
                        showAuthModal = false
                        showProfileSetup = true
                    },
                    onSkip = null,
                    showSkipButton = false
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
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
import com.example.ticketway.ui.screens.ProfileSetupScreen // New Import
import com.example.ticketway.ui.theme.TicketWayTheme
import com.example.ticketway.ui.viewmodel.AuthViewModel
import com.example.ticketway.ui.viewmodel.FixturesViewModel
import com.example.ticketway.ui.viewmodel.FixturesViewModelFactory
import com.example.ticketway.ui.viewmodel.RegistrationStep // New Import

class MainActivity : ComponentActivity() {

    private lateinit var fixturesViewModel: FixturesViewModel
    private lateinit var authViewModel: AuthViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "onCreate called")

        // Initialize ViewModels
        // Assuming DatabaseProvider and other necessary dependencies are available in your project context
        val db = DatabaseProvider.getDatabase(this)
        val fixturesFactory = FixturesViewModelFactory(db)
        fixturesViewModel = ViewModelProvider(this, fixturesFactory)[FixturesViewModel::class.java]
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        setContent {
            TicketWayTheme {
                AppContent(
                    fixturesViewModel = fixturesViewModel,
                    authViewModel = authViewModel
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
    authViewModel: AuthViewModel
) {
    // Simple state: either show auth or show home
    var showAuth by remember { mutableStateOf(true) }
    var showAuthModal by remember { mutableStateOf(false) }
    var showProfileSetup by remember { mutableStateOf(false) } // NEW: State for the second registration step

    val authState by authViewModel.authState.collectAsState()
    val registrationState by authViewModel.registrationState.collectAsState() // NEW: Observe registration step

    // Initialize: check if user is already logged in.
    LaunchedEffect(Unit) {
        Log.d("AppContent", "Initial check for logged-in user.")
        authViewModel.checkUser()
    }

    // Update when auth state changes (FINAL success/failure)
    LaunchedEffect(authState) {
        Log.d("AppContent", "Auth state changed: $authState")
        if (authState == true) {
            // User is fully authenticated and profile is complete
            showAuth = false
            showAuthModal = false
            showProfileSetup = false
        } else if (authState == false) {
            // User is explicitly logged out or failed login.
        }
    }

    // NEW: Handle transition from AuthScreen to ProfileSetupScreen
    LaunchedEffect(registrationState) {
        if (registrationState == RegistrationStep.AUTH_COMPLETE) {
            showAuth = false // Hide Auth screen
            showAuthModal = false // Hide Modal if it was showing
            showProfileSetup = true // Show Profile setup screen
        }
    }

    // Primary screen flow:
    if (showAuth) {
        Log.d("AppContent", "Showing Auth Screen (Full screen)")
        AuthScreen(
            viewModel = authViewModel,
            onAuthSuccess = {
                Log.d("AppContent", "onAuthSuccess called (Login)")
                // authState handles showAuth = false
            },
            onRegistrationSuccess = {
                Log.d("AppContent", "onRegistrationSuccess called -> Profile Setup")
                // registrationState handles the navigation to Profile Setup
            },
            onSkip = {
                Log.d("AppContent", "onSkip called - going to home")
                showAuth = false
            },
            showSkipButton = true
        )
    } else if (showProfileSetup) { // NEW: Handle Profile Setup
        Log.d("AppContent", "Showing Profile Setup Screen")
        ProfileSetupScreen(
            viewModel = authViewModel,
            onSetupComplete = {
                Log.d("AppContent", "Profile setup complete -> Home")
                // authState handles showAuth = false and showProfileSetup = false
            }
        )
    }
    else {
        // Show home screen
        Log.d("AppContent", "Showing Home Screen")
        BookingHomeScreen(
            viewModel = fixturesViewModel,
            onMatchClick = { fixture ->
                Log.d("AppContent", "Match clicked: ${fixture.teams.home.name} vs ${fixture.teams.away.name}")

                if (authState != true) {
                    Log.d("AppContent", "User not logged in - showing auth modal")
                    showAuthModal = true
                } else {
                    Log.d("AppContent", "User logged in - proceed to booking")
                    // TODO: Go to booking screen
                }
            },
            onMenuClick = {
                Log.d("AppContent", "Menu clicked")
            }
        )

        // Auth modal for booking (only visible over the Home screen)
        if (showAuthModal) {
            Log.d("AppContent", "Showing auth modal")
            ModalBottomSheet(
                onDismissRequest = {
                    Log.d("AppContent", "Auth modal dismissed")
                    showAuthModal = false
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        text = "Login Required",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Please login to book tickets",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                AuthScreen(
                    viewModel = authViewModel,
                    onAuthSuccess = {
                        Log.d("AppContent", "Modal auth success (Login)")
                        showAuthModal = false
                    },
                    // Registration inside modal is blocked by showSkipButton = false,
                    // so onRegistrationSuccess won't be called, but we include it for safety
                    onRegistrationSuccess = {
                        // If registration somehow happens here, this navigates to the main profile setup screen
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
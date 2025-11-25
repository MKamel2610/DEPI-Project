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
import com.example.ticketway.data.repository.BookingRepository
import com.example.ticketway.data.repository.UserRepository
import com.example.ticketway.ui.booking.BookingViewModel
import com.example.ticketway.ui.screens.AuthScreen
import com.example.ticketway.ui.screens.BookingHomeScreen
import com.example.ticketway.ui.screens.ProfileSetupScreen
import com.example.ticketway.ui.screens.booking.TierSelectionScreen
import com.example.ticketway.ui.screens.booking.BookingSummaryScreen
// Removed import: PaymentWebviewScreen
import com.example.ticketway.ui.screens.profile.ProfileScreen
import com.example.ticketway.ui.theme.TicketWayTheme
import com.example.ticketway.ui.user.UserViewModel
import com.example.ticketway.ui.viewmodel.AuthViewModel
import com.example.ticketway.ui.viewmodel.FixturesViewModel
import com.example.ticketway.ui.viewmodel.FixturesViewModelFactory
import com.example.ticketway.ui.viewmodel.RegistrationStep
import androidx.activity.compose.BackHandler
import androidx.lifecycle.ViewModel

// Reverted to single repository constructor for BookingViewModel
class BookingViewModelFactory(private val bookingRepo: BookingRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            return BookingViewModel(bookingRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


class MainActivity : ComponentActivity() {

    private lateinit var fixturesViewModel: FixturesViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var bookingViewModel: BookingViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "onCreate called")

        // Initialize Repositories and Factories
        val db = DatabaseProvider.getDatabase(this)
        val fixturesFactory = FixturesViewModelFactory(db)
        val bookingRepo = BookingRepository()
        // Removed PaymobRepo init

        val bookingFactory = BookingViewModelFactory(bookingRepo) // Reverted to single factory

        // Initialize ViewModels
        fixturesViewModel = ViewModelProvider(this, fixturesFactory)[FixturesViewModel::class.java]
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        bookingViewModel = ViewModelProvider(this, bookingFactory)[BookingViewModel::class.java]


        setContent {
            TicketWayTheme {
                AppContent(
                    fixturesViewModel = fixturesViewModel,
                    authViewModel = authViewModel,
                    userViewModel = userViewModel,
                    bookingViewModel = bookingViewModel
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
    userViewModel: UserViewModel,
    bookingViewModel: BookingViewModel
) {
    // Primary navigation states
    var showAuth by remember { mutableStateOf(true) }
    var showAuthModal by remember { mutableStateOf(false) }
    var showProfileSetup by remember { mutableStateOf(false) }
    var showTierSelection by remember { mutableStateOf(false) }
    var showBookingSummary by remember { mutableStateOf(false) }
    // Removed showPaymentWebview state
    var currentTab by remember { mutableStateOf("home") }

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
            userViewModel.loadUser()
        } else if (authState == false) {
            showAuth = true
            currentTab = "home"
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

    // Removed LaunchedEffect(paymobUrl)

    // --- Back Stack Management FIX (Reverted to stable state) ---

    val isDeepScreenActive = showTierSelection || showProfileSetup || showAuthModal || currentTab == "Profile" || showBookingSummary

    BackHandler(enabled = isDeepScreenActive) {
        when {
            showBookingSummary -> {
                // Back from Summary goes to Tier Selection
                showBookingSummary = false
                showTierSelection = true
            }
            showTierSelection -> {
                // Back from Tier Selection goes to Home, clearing booking state
                showTierSelection = false
                bookingViewModel.clearBookingState()
            }
            showProfileSetup -> {
                authViewModel.logout()
            }
            showAuthModal -> {
                showAuthModal = false
            }
            currentTab == "Profile" -> {
                currentTab = "home"
            }
        }
    }

    // --- Logout Functionality ---
    val onLogout: () -> Unit = {
        authViewModel.logout()
    }

    // --- Screen Logic ---
    if (showAuth) {
        AuthScreen(
            viewModel = authViewModel,
            onAuthSuccess = { /* ... */ },
            onRegistrationSuccess = { /* ... */ },
            onSkip = { showAuth = false },
            showSkipButton = true
        )
    } else if (showProfileSetup) {
        ProfileSetupScreen(
            viewModel = authViewModel,
            onSetupComplete = { /* ... */ }
        )
    } else if (showBookingSummary) {
        BookingSummaryScreen(
            bookingViewModel = bookingViewModel,
            onProcedeToPayment = {
                // FINAL STEP: Navigating home as payment is removed
                showBookingSummary = false
                currentTab = "home"
                bookingViewModel.clearBookingState()
            },
            onBack = {
                showBookingSummary = false
                showTierSelection = true
            }
        )
    } else if (showTierSelection) {
        TierSelectionScreen(
            bookingViewModel = bookingViewModel,
            userViewModel = userViewModel,
            onBookingConfirmed = {
                showTierSelection = false
                showBookingSummary = true
            },
            onBack = {
                showTierSelection = false
            }
        )
    } else {
        // Main Home Screen flow with Bottom Navigation
        when (currentTab) {
            "home", "My Tickets" -> {
                BookingHomeScreen(
                    viewModel = fixturesViewModel,
                    currentTab = currentTab,
                    onTabSelected = { newTab ->
                        currentTab = newTab
                        if (newTab == "Profile") {
                            if (authState != true) {
                                showAuthModal = true
                                currentTab = "home"
                            }
                        }
                    },
                    onMatchClick = { fixture ->
                        if (authState != true) {
                            showAuthModal = true
                        } else {
                            bookingViewModel.startBooking(fixture)
                            showTierSelection = true
                        }
                    }
                )
            }
            "Profile" -> {
                ProfileScreen(
                    viewModel = userViewModel,
                    onBack = { currentTab = "home" },
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
                    Text(text = if (currentTab == "Profile") "Login Required" else "Booking Requires Login", style = MaterialTheme.typography.headlineSmall)
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
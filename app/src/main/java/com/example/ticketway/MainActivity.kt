package com.example.ticketway

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ticketway.data.local.DatabaseProvider
import com.example.ticketway.data.repository.BookingRepository
import com.example.ticketway.ui.booking.BookingViewModel
import com.example.ticketway.ui.components.homescreen.BottomNavigationBar
import com.example.ticketway.ui.screens.AuthScreen
import com.example.ticketway.ui.screens.BookingHomeScreen
import com.example.ticketway.ui.screens.ProfileSetupScreen
import com.example.ticketway.ui.screens.booking.TierSelectionScreen
import com.example.ticketway.ui.screens.booking.BookingSummaryScreen
import com.example.ticketway.ui.screens.MockPaymentScreen
import com.example.ticketway.ui.screens.profile.ProfileScreen
import com.example.ticketway.ui.screens.MyTicketsScreen
import com.example.ticketway.ui.theme.TicketWayTheme
import com.example.ticketway.ui.ui.theme.DarkText
import com.example.ticketway.ui.user.UserViewModel
import com.example.ticketway.ui.viewmodel.AuthViewModel
import com.example.ticketway.ui.viewmodel.FixturesViewModel
import com.example.ticketway.ui.viewmodel.FixturesViewModelFactory
import com.example.ticketway.ui.viewmodel.MyTicketsViewModel
import com.example.ticketway.ui.viewmodel.RegistrationStep
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import com.example.ticketway.data.model.BookingItem
import com.example.ticketway.ui.screens.TicketDetailsScreen
import com.example.ticketway.ui.viewmodel.BookingViewModelFactory
import com.example.ticketway.ui.viewmodel.MyTicketsViewModelFactory
class MainActivity : ComponentActivity() {

    private lateinit var fixturesViewModel: FixturesViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var bookingViewModel: BookingViewModel
    private lateinit var myTicketsViewModel: MyTicketsViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "onCreate called")

        // Initialize Repositories and Factories
        val db = DatabaseProvider.getDatabase(this)
        val fixturesFactory = FixturesViewModelFactory(db)
        val bookingRepo = BookingRepository()

        val bookingFactory = BookingViewModelFactory(bookingRepo)
        val myTicketsFactory = MyTicketsViewModelFactory(bookingRepo)

        // Initialize ViewModels
        fixturesViewModel = ViewModelProvider(this, fixturesFactory)[FixturesViewModel::class.java]
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        bookingViewModel = ViewModelProvider(this, bookingFactory)[BookingViewModel::class.java]
        myTicketsViewModel = ViewModelProvider(this, myTicketsFactory)[MyTicketsViewModel::class.java]


        setContent {
            TicketWayTheme {
                AppContent(
                    fixturesViewModel = fixturesViewModel,
                    authViewModel = authViewModel,
                    userViewModel = userViewModel,
                    bookingViewModel = bookingViewModel,
                    myTicketsViewModel = myTicketsViewModel
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
    bookingViewModel: BookingViewModel,
    myTicketsViewModel: MyTicketsViewModel
) {
    // Primary navigation states
    var showAuth by remember { mutableStateOf(true) }
    var showAuthModal by remember { mutableStateOf(false) }
    var showProfileSetup by remember { mutableStateOf(false) }
    var showTierSelection by remember { mutableStateOf(false) }
    var showBookingSummary by remember { mutableStateOf(false) }
    var showMockPaymentScreen by remember { mutableStateOf(false) }
    var showTicketDetails by remember { mutableStateOf(false) }
    var selectedTicket by remember { mutableStateOf<BookingItem?>(null) }
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

    // Watch for successful save after payment attempt
    val isBookingSuccess by bookingViewModel.isSuccess.collectAsState()
    LaunchedEffect(isBookingSuccess) {
        if (isBookingSuccess) {
            // Success navigation: Go to My Tickets screen and refresh the list
            showMockPaymentScreen = false
            currentTab = "My Tickets"
            myTicketsViewModel.loadTickets()
            bookingViewModel.clearBookingState()
        }
    }

    // --- Back Stack Management ---

    // The handler should be active if ANY non-home-tab or deep screen is showing.
    // This includes the Profile and My Tickets main tabs for custom navigation back to 'home'.
    val isCustomBackNavigationActive = showTierSelection || showProfileSetup || showAuthModal || showBookingSummary || showMockPaymentScreen || showTicketDetails || currentTab == "Profile" || currentTab == "My Tickets"

    BackHandler(enabled = isCustomBackNavigationActive) {
        when {
            // 1. Deep Screen Back Navigation
            showTicketDetails -> {
                showTicketDetails = false
                selectedTicket = null
            }
            showMockPaymentScreen -> {
                showMockPaymentScreen = false
                showBookingSummary = true
            }
            showBookingSummary -> {
                showBookingSummary = false
                showTierSelection = true
            }
            showTierSelection -> {
                showTierSelection = false
                bookingViewModel.clearBookingState()
            }
            showProfileSetup -> {
                authViewModel.logout()
            }
            showAuthModal -> {
                showAuthModal = false
            }

            // 2. Tab Navigation Back to Home (Per user request)
            currentTab == "Profile" -> {
                currentTab = "home"
            }
            currentTab == "My Tickets" -> { // NEW: Handle back from My Tickets
                currentTab = "home"
            }

            // Note: If currentTab is "home" and no deep screen is active, isCustomBackNavigationActive is false,
            // and the system handles the exit.
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
    } else if (showMockPaymentScreen) {
        MockPaymentScreen(
            viewModel = bookingViewModel,
            onPaymentSuccess = {
                // Success action handled by LaunchedEffect(isBookingSuccess)
            },
            onPaymentFailure = {
                showMockPaymentScreen = false
                showBookingSummary = true
            },
            onClose = {
                showMockPaymentScreen = false
                showBookingSummary = true
            }
        )
    } else if (showBookingSummary) {
        BookingSummaryScreen(
            bookingViewModel = bookingViewModel,
            onProcedeToPayment = {
                // Final button navigates to the Mock Payment screen
                showBookingSummary = false
                showMockPaymentScreen = true
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
    } else if (showTicketDetails && selectedTicket != null) { // Dedicated view for Ticket Details
        TicketDetailsScreen(
            booking = selectedTicket!!,
            onBack = {
                showTicketDetails = false
                selectedTicket = null
            }
        )
    } else {
        // Main Home Screen flow with Bottom Navigation and Shared Header
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "TicketWay",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                    },
                    actions = {
                        IconButton(onClick = { fixturesViewModel.refresh() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = DarkText)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    selectedTab = currentTab,
                    onTabSelected = { newTab ->
                        currentTab = newTab
                        if (newTab == "Profile") {
                            if (authState != true) {
                                showAuthModal = true
                                currentTab = "home"
                            }
                        } else if (newTab == "My Tickets") {
                            myTicketsViewModel.loadTickets()
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White)
            ) {
                when (currentTab) {
                    "home" -> {
                        BookingHomeScreen(
                            viewModel = fixturesViewModel,
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
                    "My Tickets" -> {
                        MyTicketsScreen(
                            viewModel = myTicketsViewModel,
                            onTicketClick = { booking ->
                                selectedTicket = booking
                                showTicketDetails = true
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
            }
        }

        // Auth modal
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
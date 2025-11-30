package com.example.ticketway

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketway.ui.booking.BookingViewModel
import com.example.ticketway.ui.components.homescreen.BottomNavigationBar
import com.example.ticketway.ui.screens.AuthScreen
import com.example.ticketway.ui.screens.BookingHomeScreen
import com.example.ticketway.ui.screens.ProfileSetupScreen
import com.example.ticketway.ui.screens.TierSelectionScreen
import com.example.ticketway.ui.screens.BookingSummaryScreen
import com.example.ticketway.ui.screens.MockPaymentScreen
import com.example.ticketway.ui.screens.ProfileScreen
import com.example.ticketway.ui.screens.MyTicketsScreen
import com.example.ticketway.ui.theme.TicketWayTheme
import com.example.ticketway.ui.user.UserViewModel
import com.example.ticketway.ui.viewmodel.AuthViewModel
import com.example.ticketway.ui.viewmodel.FixturesViewModel
import com.example.ticketway.ui.viewmodel.MyTicketsViewModel
import com.example.ticketway.ui.viewmodel.RegistrationStep
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import com.example.ticketway.data.model.BookingItem
import com.example.ticketway.ui.screens.TicketDetailsScreen
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Search
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import com.example.ticketway.ui.screens.BookingSummaryScreen
import com.example.ticketway.ui.screens.ProfileScreen
import com.example.ticketway.ui.screens.TierSelectionScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @delegate:RequiresApi(Build.VERSION_CODES.O)
    private val fixturesViewModel: FixturesViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val bookingViewModel: BookingViewModel by viewModels()
    private val myTicketsViewModel: MyTicketsViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "onCreate called")

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
    val coroutineScope = rememberCoroutineScope()

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

    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val homeListState = remember { LazyListState() }

    val authState by authViewModel.authState.collectAsState()
    val registrationState by authViewModel.registrationState.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.checkUser()
    }

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

    LaunchedEffect(registrationState) {
        if (registrationState == RegistrationStep.AUTH_COMPLETE) {
            showAuth = false
            showAuthModal = false
            showProfileSetup = true
        }
    }

    val isBookingSuccess by bookingViewModel.isSuccess.collectAsState()
    LaunchedEffect(isBookingSuccess) {
        if (isBookingSuccess) {
            showMockPaymentScreen = false
            currentTab = "My Tickets"
            myTicketsViewModel.loadTickets()
            bookingViewModel.clearBookingState()
        }
    }

    // --- Back Stack Management ---

    val isCustomBackNavigationActive = showTierSelection || showProfileSetup || showAuthModal || showBookingSummary || showMockPaymentScreen || showTicketDetails || isSearchActive || currentTab == "Profile" || currentTab == "My Tickets"

    BackHandler(enabled = isCustomBackNavigationActive) {
        when {
            isSearchActive -> {
                isSearchActive = false
                searchQuery = ""
            }
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
            currentTab == "My Tickets" -> {
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
    } else if (showMockPaymentScreen) {
        MockPaymentScreen(
            viewModel = bookingViewModel,
            onPaymentSuccess = {
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
    } else if (showTicketDetails && selectedTicket != null) {
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
                if (isSearchActive) {
                    SearchTopBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearchClose = {
                            isSearchActive = false
                            searchQuery = ""
                        }
                    )
                } else {
                    // Original Top Bar
                    TopAppBar(
                        title = {
                            Text(
                                "TicketWay",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        actions = {
                            IconButton(onClick = { isSearchActive = true }) { // Launch Search Bar
                                Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurface)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                    )
                }
            },
            bottomBar = {
                BottomNavigationBar(
                    selectedTab = currentTab,
                    onTabSelected = { newTab ->
                        if (newTab == "home" && currentTab == "home") {
                            coroutineScope.launch {
                                homeListState.animateScrollToItem(0)
                            }
                        }
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
                    .background(MaterialTheme.colorScheme.background)
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
                            },
                            listState = homeListState,
                            searchQuery = searchQuery
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
                onDismissRequest = { showAuthModal = false },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(text = if (currentTab == "Profile") "Login Required" else "Booking Requires Login", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Please login to continue", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClose: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .height(56.dp)
                .padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onSearchClose) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Close Search", tint = MaterialTheme.colorScheme.onSurface)
            }

            TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search by team name...") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear Search", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

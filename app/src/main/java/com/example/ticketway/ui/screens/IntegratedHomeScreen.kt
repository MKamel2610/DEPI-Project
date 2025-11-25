package com.example.ticketway.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketway.data.network.model.fixtures.FixtureItem
import com.example.ticketway.ui.components.*
import com.example.ticketway.ui.components.homescreen.BottomNavigationBar
import com.example.ticketway.ui.components.homescreen.LeagueFilterRow
import com.example.ticketway.ui.viewmodel.FixturesViewModel
import com.example.ticketway.ui.ui.theme.*


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHomeScreen( // Renamed parameters to align with use in MainActivity
    viewModel: FixturesViewModel,
    currentTab: String, // NEW: Current selected tab state
    onTabSelected: (String) -> Unit, // NEW: Callback for tab clicks
    onMatchClick: (FixtureItem) -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val fixturesState by viewModel.fixtures.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedLeagueId by remember { mutableStateOf<Int?>(null) }

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
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = DarkText
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = DarkText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedTab = currentTab, // Use current tab state
                onTabSelected = onTabSelected // Pass the callback
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            // Check the current tab and display appropriate content
            when (currentTab) {
                "home" -> MatchListContent(
                    fixturesState = fixturesState?.response ?: emptyList(),
                    isLoading = isLoading,
                    selectedLeagueId = selectedLeagueId,
                    onLeagueSelected = { selectedLeagueId = it },
                    onMatchClick = onMatchClick
                )
                "My Tickets" -> EmptyTicketsContent() // Placeholder for My Tickets
            }
        }
    }
}

// NEW: Extracted Match List Content Composable
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MatchListContent(
    fixturesState: List<FixtureItem>,
    isLoading: Boolean,
    selectedLeagueId: Int?,
    onLeagueSelected: (Int?) -> Unit,
    onMatchClick: (FixtureItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // League Filter Chips
        item {
            Spacer(modifier = Modifier.height(8.dp))
            LeagueFilterRow(
                fixtures = fixturesState,
                selectedLeagueId = selectedLeagueId,
                onLeagueSelected = onLeagueSelected
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Loading State
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(color = PrimaryGreen)
                        Text(
                            "Loading matches...",
                            color = LightText,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Matches grouped by league
        if (!isLoading) {
            val filteredFixtures = if (selectedLeagueId != null) {
                fixturesState.filter { it.league.id == selectedLeagueId }
            } else {
                fixturesState
            }

            val groupedFixtures = filteredFixtures.groupBy { it.league.id }

            if (groupedFixtures.isEmpty()) {
                item {
                    NoMatchesContent()
                }
            } else {
                groupedFixtures.forEach { (_, leagueFixtures) ->
                    item {
                        BookingLeagueSection(
                            fixtures = leagueFixtures,
                            onMatchClick = onMatchClick,
                            onLeagueClick = { /* TODO: Navigate to league details */ }
                        )
                    }
                }
            }
        }

        // Bottom spacing
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// NEW: Extracted placeholder for "My Tickets"
@Composable
fun EmptyTicketsContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "🎟️",
                fontSize = 64.sp
            )
            Text(
                text = "No Tickets Booked Yet",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )
            Text(
                text = "Book a match to see your tickets here.",
                fontSize = 14.sp,
                color = LightText,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

// NEW: Extracted placeholder for "No Matches"
@Composable
fun NoMatchesContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "⚽",
                fontSize = 64.sp
            )
            Text(
                "No matches available",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText
            )
            Text(
                "Check back later for upcoming fixtures",
                fontSize = 14.sp,
                color = LightText
            )
        }
    }
}
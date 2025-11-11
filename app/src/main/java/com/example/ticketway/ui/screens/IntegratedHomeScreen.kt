package com.example.ticketway.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import com.example.ticketway.ui.viewmodel.FixturesViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val PrimaryGreen = Color(0xFF009688)
val DarkText = Color(0xFF9F9F9F)
val LightText = Color(0xFF757575)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntegratedHomeScreen(
    viewModel: FixturesViewModel,
    onMatchClick: (FixtureItem) -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val fixturesState by viewModel.fixtures.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedLeagueId by remember { mutableStateOf<Int?>(null) }

    // Load fixtures when date changes
    LaunchedEffect(selectedDate) {
        val dateStr = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        viewModel.loadFixtures(dateStr)
    }

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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavigationBar()
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
        ) {
            // League Filter Chips
            item {
                Spacer(modifier = Modifier.height(8.dp))
                LeagueFilterRow(
                    fixtures = fixturesState?.response ?: emptyList(),
                    selectedLeagueId = selectedLeagueId,
                    onLeagueSelected = { selectedLeagueId = it }
                )
            }

            // Date Selector
            item {
                DateSelectorRow(
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    onCalendarClick = {
                        // TODO: Open date picker dialog
                    }
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
                        CircularProgressIndicator(color = PrimaryGreen)
                    }
                }
            }

            // Matches grouped by league
            if (!isLoading) {
                fixturesState?.response?.let { fixtures ->
                    val filteredFixtures = if (selectedLeagueId != null) {
                        fixtures.filter { it.league.id == selectedLeagueId }
                    } else {
                        fixtures
                    }

                    val groupedFixtures = filteredFixtures.groupBy { it.league.id }

                    if (groupedFixtures.isEmpty()) {
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
                                    Text(
                                        "⚽",
                                        fontSize = 48.sp
                                    )
                                    Text(
                                        "No matches found",
                                        color = LightText,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    } else {
                        groupedFixtures.forEach { (_, leagueFixtures) ->
                            item {
                                LeagueMatchCard(
                                    fixtures = leagueFixtures,
                                    onMatchClick = onMatchClick,
                                    onLeagueClick = {
                                        // TODO: Navigate to league details
                                    }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
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
}
package com.example.ticketway.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketway.data.network.model.fixtures.FixtureItem
import com.example.ticketway.ui.components.*
import com.example.ticketway.ui.components.homescreen.BookingLeagueSection
import com.example.ticketway.ui.components.homescreen.LeagueFilterRow
import com.example.ticketway.ui.viewmodel.FixturesViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHomeScreen(
    viewModel: FixturesViewModel,
    onMatchClick: (FixtureItem) -> Unit = {},
    listState: LazyListState = rememberLazyListState(),
    searchQuery: String = ""
) {
    val fixturesState by viewModel.fixtures.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedLeagueId by remember { mutableStateOf<Int?>(null) }

    val pullRefreshState = rememberPullToRefreshState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PullToRefreshBox(
            isRefreshing = isLoading,
            onRefresh = { viewModel.refresh() },
            state = pullRefreshState,
            modifier = Modifier.fillMaxSize()
        ) {
            MatchListContent(
                fixturesState = fixturesState?.response ?: emptyList(),
                isLoading = isLoading,
                selectedLeagueId = selectedLeagueId,
                onLeagueSelected = { selectedLeagueId = it },
                onMatchClick = onMatchClick,
                listState = listState,
                searchQuery = searchQuery
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MatchListContent(
    fixturesState: List<FixtureItem>,
    isLoading: Boolean,
    selectedLeagueId: Int?,
    onLeagueSelected: (Int?) -> Unit,
    onMatchClick: (FixtureItem) -> Unit,
    listState: LazyListState,
    searchQuery: String
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            LeagueFilterRow(
                fixtures = fixturesState,
                selectedLeagueId = selectedLeagueId,
                onLeagueSelected = onLeagueSelected
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

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
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Text(
                            "Loading matches...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        if (!isLoading) {
            val leagueFilteredFixtures = if (selectedLeagueId != null) {
                fixturesState.filter { it.league.id == selectedLeagueId }
            } else {
                fixturesState
            }

            val filteredFixtures = if (searchQuery.isNotBlank()) {
                leagueFilteredFixtures.filter { fixture ->
                    val query = searchQuery.trim()
                    fixture.teams.home.name.contains(query, ignoreCase = true) ||
                            fixture.teams.away.name.contains(query, ignoreCase = true)
                }
            } else {
                leagueFilteredFixtures
            }

            val groupedFixtures = filteredFixtures.groupBy { it.league.id }

            if (groupedFixtures.isEmpty()) {
                item { NoMatchesContent() }
            } else {
                groupedFixtures.forEach { (_, leagueFixtures) ->
                    item {
                        BookingLeagueSection(
                            fixtures = leagueFixtures,
                            onMatchClick = onMatchClick,
                            onLeagueClick = { }
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
fun EmptyTicketsContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon( // Replaced emoji
                Icons.Default.ConfirmationNumber,
                contentDescription = "No Tickets Icon",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "No Tickets Booked Yet",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Book a match to see your tickets here.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

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
            Icon( // Replaced emoji
                Icons.Default.SportsSoccer,
                contentDescription = "No Matches Icon",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "No matches available",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Check back later for upcoming fixtures",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
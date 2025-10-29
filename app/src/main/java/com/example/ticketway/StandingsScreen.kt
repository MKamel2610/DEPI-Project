package com.example.ticketway.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ticketway.ui.viewmodel.StandingsViewModel

@Composable
fun StandingsScreen(viewModel: StandingsViewModel) {
    val standingsState by viewModel.standings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Load once
    LaunchedEffect(Unit) {
        viewModel.loadStandings(leagueId = 39, season = 2023)
    }

    when {
        isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        error != null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: $error")
            }
        }
        standingsState?.response?.isNotEmpty() == true -> {
            val table = standingsState!!.response.first().league.standings.firstOrNull().orEmpty()

            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                items(table.size) { index ->
                    val teamStanding = table[index]
                    Text(
                        text = "${teamStanding.rank}. ${teamStanding.team.name} - ${teamStanding.points} pts",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
        else -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No standings found.")
            }
        }
    }
}

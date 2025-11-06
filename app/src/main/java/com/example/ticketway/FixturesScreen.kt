package com.example.ticketway.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ticketway.ui.viewmodel.FixturesViewModel

@Composable
fun FixturesScreen(viewModel: FixturesViewModel) {
    val fixturesState by viewModel.fixtures.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Load fixtures on first composition
    LaunchedEffect(Unit) {
        viewModel.loadFixtures("2025-11-01")
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
        fixturesState?.response?.isNotEmpty() == true -> {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                items(fixturesState!!.response.size) { index ->
                    val fixture = fixturesState!!.response[index]
                    Text(
                        text = "${fixture.teams.home.name} vs ${fixture.teams.away.name}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
        else -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No fixtures found.")
            }
        }
    }
}

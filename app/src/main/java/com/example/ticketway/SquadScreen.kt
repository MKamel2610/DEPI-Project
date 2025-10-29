package com.example.ticketway.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.ticketway.ui.viewmodel.SquadViewModel

@Composable
fun SquadScreen(viewModel: SquadViewModel) {
    val squadState by viewModel.squad.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Example: Load squad for team ID 33 (Manchester United)
    LaunchedEffect(Unit) {
        viewModel.loadSquad(33)
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

        squadState?.response?.isNotEmpty() == true -> {
            val squad = squadState!!.response.first()
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(
                    text = squad.team.name,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn {
                    items(squad.players) { player ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(8.dp)
                            ) {
//                                Image(
//                                    painter = rememberAsyncImagePainter(player.photo),
//                                    contentDescription = player.name,
//                                    modifier = Modifier
//                                        .size(48.dp)
//                                        .padding(end = 8.dp),
//                                    contentScale = ContentScale.Crop
//                                )
                                Column {
                                    Text(player.name, style = MaterialTheme.typography.bodyLarge)
                                    Text(
                                        "Age: ${player.age ?: "?"} | Position: ${player.position}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        else -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No squad data available.")
            }
        }
    }
}

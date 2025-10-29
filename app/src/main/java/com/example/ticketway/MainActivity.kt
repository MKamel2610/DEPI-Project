package com.example.ticketway

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.ticketway.data.local.DatabaseProvider
import com.example.ticketway.ui.screens.FixturesScreen
import com.example.ticketway.ui.screens.StandingsScreen
import com.example.ticketway.ui.screens.SquadScreen
import com.example.ticketway.ui.theme.TicketWayTheme
import com.example.ticketway.ui.viewmodel.*

class MainActivity : ComponentActivity() {

    private lateinit var fixturesViewModel: FixturesViewModel
    private lateinit var standingsViewModel: StandingsViewModel
    private lateinit var squadViewModel: SquadViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseProvider.getDatabase(this)
        val fixturesFactory = FixturesViewModelFactory(db)
        val standingsFactory = StandingsViewModelFactory(db)
        val squadFactory = SquadViewModelFactory(db)

        fixturesViewModel = ViewModelProvider(this, fixturesFactory)[FixturesViewModel::class.java]
        standingsViewModel = ViewModelProvider(this, standingsFactory)[StandingsViewModel::class.java]
        squadViewModel = ViewModelProvider(this, squadFactory)[SquadViewModel::class.java]

        setContent {
            TicketWayTheme {
                var selectedScreen by remember { mutableStateOf("fixtures") }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("TicketWay") },
                            actions = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(end = 12.dp)
                                ) {
                                    Button(
                                        onClick = { selectedScreen = "fixtures" },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (selectedScreen == "fixtures")
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.secondaryContainer
                                        )
                                    ) { Text("Fixtures") }

                                    Button(
                                        onClick = { selectedScreen = "standings" },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (selectedScreen == "standings")
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.secondaryContainer
                                        )
                                    ) { Text("Standings") }

                                    Button(
                                        onClick = { selectedScreen = "squad" },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (selectedScreen == "squad")
                                                MaterialTheme.colorScheme.primary
                                            else
                                                MaterialTheme.colorScheme.secondaryContainer
                                        )
                                    ) { Text("Squad") }
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        when (selectedScreen) {
                            "fixtures" -> FixturesScreen(viewModel = fixturesViewModel)
                            "standings" -> StandingsScreen(viewModel = standingsViewModel)
                            "squad" -> SquadScreen(viewModel = squadViewModel)
                        }
                    }
                }
            }
        }
    }
}

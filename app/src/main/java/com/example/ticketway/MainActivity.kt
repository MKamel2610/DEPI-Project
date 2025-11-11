package com.example.ticketway

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.ticketway.data.local.DatabaseProvider
import com.example.ticketway.ui.screens.IntegratedHomeScreen
import com.example.ticketway.ui.theme.TicketWayTheme
import com.example.ticketway.ui.viewmodel.FixturesViewModel
import com.example.ticketway.ui.viewmodel.FixturesViewModelFactory

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseProvider.getDatabase(this)
        val fixturesFactory = FixturesViewModelFactory(db)
        val fixturesViewModel = ViewModelProvider(this, fixturesFactory)[FixturesViewModel::class.java]

        setContent {
            TicketWayTheme {
                IntegratedHomeScreen(
                    viewModel = fixturesViewModel,
                    onMatchClick = { fixture ->
                        // TODO: Navigate to match details
                    },
                    onMenuClick = {
                        // TODO: Open drawer/menu
                    }
                )
            }
        }
    }
}
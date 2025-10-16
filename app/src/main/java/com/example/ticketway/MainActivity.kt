package com.example.ticketway

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.ticketway.ui.viewmodel.FixturesViewModel

class MainActivity : ComponentActivity() {

    private val viewModel = FixturesViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launchWhenStarted {
            viewModel.fetchFixtures("2025-10-16")
        }
    }
}

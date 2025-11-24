package com.example.ticketway.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ticketway.ui.booking.BookingViewModel
import com.example.ticketway.ui.components.tierselectionscreen.ConfirmCancelBar
import com.example.ticketway.ui.components.tierselectionscreen.TierOptionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TierSelectionScreen(
    navController: NavController,
    bookingViewModel: BookingViewModel,
    fixtureId: Int,
    homeTeam: String,
    awayTeam: String,
    stadiumName: String,
    matchDate: String,
    onBack: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        bookingViewModel.clearTempBooking()
    }

    var reg by remember { mutableStateOf(0) }
    var premium by remember { mutableStateOf(0) }
    var vip by remember { mutableStateOf(0) }

    val regPrice = 100
    val premiumPrice = 200
    val vipPrice = 300
    val total = (reg * regPrice) + (premium * premiumPrice) + (vip * vipPrice)

    LaunchedEffect(reg, premium, vip) {
        bookingViewModel.updateRegular(reg)
        bookingViewModel.updatePremium(premium)
        bookingViewModel.updateVip(vip)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Tier", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = {
                        bookingViewModel.clearTempBooking()
                        onBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Error message
            bookingViewModel.errorMsg.collectAsState().value?.let { msg ->
                Text(text = msg, color = Color.Red)
            }

            // Loading indicator
            if (bookingViewModel.isLoading.collectAsState().value) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            // Tier Cards
            TierOptionCard("Regular", regPrice, reg) { reg = it }
            TierOptionCard("Premium", premiumPrice, premium) { premium = it }
            TierOptionCard("VIP", vipPrice, vip) { vip = it }

            // Spacer قصير قبل الزرار
            Spacer(modifier = Modifier.height(16.dp))

            // Confirm / Cancel Bar
            if (total > 0) {
                ConfirmCancelBar(
                    totalPrice = total,
                    bookingViewModel = bookingViewModel,
                    fixtureId = fixtureId,
                    homeTeam = homeTeam,
                    awayTeam = awayTeam,
                    stadiumName = stadiumName,
                    matchDate = matchDate,
                    onNavigateToSummary = {
                        bookingViewModel.tempBooking = BookingViewModel.TempBooking(
                            fixtureId, homeTeam, awayTeam, stadiumName, matchDate,
                            reg, premium, vip, total
                        )
                        reg = 0
                        premium = 0
                        vip = 0
                        navController.navigate("booking_summary")
                    },
                    onCancel = {
                        reg = 0
                        premium = 0
                        vip = 0
                        bookingViewModel.clearTempBooking()
                    }
                )
            }
        }
    }
}

package com.example.ticketway.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ticketway.data.model.BookingItem
import com.example.ticketway.ui.booking.BookingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingSummaryScreen(
    navController: NavController,
    bookingViewModel: BookingViewModel
) {
    val isLoading by bookingViewModel.isLoading.collectAsState()

    // حنعرض بس الـ tempBooking الحالي
    val tempBooking = bookingViewModel.tempBooking

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Booking Summary", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = {
                        // لما نرجع لصفحة الحجز، نمسح الحجز المؤقت
                        bookingViewModel.clearTempBooking()
                        navController.popBackStack()
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
                .padding(16.dp)
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (tempBooking == null && !isLoading) {
                Text(
                    text = "لا يوجد حجز حالياً",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                tempBooking?.let { booking ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(vertical = 12.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 180.dp)
                                .padding(8.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${booking.homeTeam}  vs  ${booking.awayTeam}",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "${booking.stadiumName} • ${booking.matchDate}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                if (booking.regularCount > 0) Text("Regular: ${booking.regularCount} x 100 EGP", color = Color.Black)
                                if (booking.premiumCount > 0) Text("Premium: ${booking.premiumCount} x 200 EGP", color = Color.Black)
                                if (booking.vipCount > 0) Text("VIP: ${booking.vipCount} x 300 EGP", color = Color.Black)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Total: ${booking.totalPrice} EGP",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF009688)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        // Navigate to PaymentScreen
                                        navController.navigate("payment_screen")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009688)),
                                    modifier = Modifier.fillMaxWidth(0.7f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Continue to Payment", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}





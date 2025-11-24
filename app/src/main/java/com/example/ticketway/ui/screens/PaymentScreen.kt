package com.example.ticketway.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ticketway.ui.booking.BookingViewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

val mainColor = Color(0xFF009688)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavController,
    bookingViewModel: BookingViewModel,
    onStartStripePayment: (amountInCents: Int) -> Unit = {}
) {
    val booking = bookingViewModel.tempBooking
    val totalPrice = booking?.totalPrice ?: 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("booking_summary")
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ------------- Review Card -------------
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {

                    Text(
                        text = "Booking Review",
                        style = MaterialTheme.typography.titleMedium,
                        color = mainColor,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Teams: ${booking?.homeTeam} vs ${booking?.awayTeam}", color = Color.Black)
                    Text("Stadium: ${booking?.stadiumName}", color = Color.Black)
                    Text("Match Date: ${booking?.matchDate}", color = Color.Black)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Total Price : $totalPrice EGP",
                        fontWeight = FontWeight.Bold,
                        color = mainColor,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            // ------------- Stripe Card -------------
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF635BFF) // Stripe Blue
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Stripe",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onStartStripePayment(totalPrice * 100) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text(
                            "Continue with Stripe",
                            color = Color(0xFF635BFF),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


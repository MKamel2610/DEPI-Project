package com.example.ticketway.ui.components.tierselectionscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketway.ui.booking.BookingViewModel
import com.example.ticketway.ui.ui.theme.DarkText
import com.example.ticketway.ui.ui.theme.LightText
import com.example.ticketway.ui.ui.theme.PrimaryGreen

@Composable
fun BookingBottomBar(
    bookingViewModel: BookingViewModel,
    onNavigateToSummary: () -> Unit
) {
    val totalPrice by bookingViewModel.totalPrice.collectAsState()
    val totalTickets by bookingViewModel.totalTickets.collectAsState()

    val isBookingValid = totalTickets > 0 && totalTickets <= 4

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            // FIX: Apply system navigation bar padding to push content up
            .windowInsetsPadding(WindowInsets.navigationBars),
        color = Color.White,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Price Display
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Total Price",
                    fontSize = 12.sp,
                    color = LightText
                )
                Text(
                    text = "EGP ${"%.2f".format(totalPrice.toDouble())}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }

            // Confirm Button
            Button(
                onClick = onNavigateToSummary,
                enabled = isBookingValid,
                modifier = Modifier
                    .width(180.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text(
                    text = if (totalTickets == 0) "Select Tickets" else "Proceed to Payment",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
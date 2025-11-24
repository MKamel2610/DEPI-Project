package com.example.ticketway.ui.components.tierselectionscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ticketway.ui.booking.BookingViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ConfirmCancelBar(
    totalPrice: Int,
    bookingViewModel: BookingViewModel,
    fixtureId: Int,
    homeTeam: String,
    awayTeam: String,
    stadiumName: String,
    matchDate: String,
    onNavigateToSummary: () -> Unit,
    onCancel: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Button(
            onClick = onCancel,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
        ) {
            Text("Cancel")
        }

        Button(
            onClick = {
                bookingViewModel.saveBooking(
                    fixtureId,
                    homeTeam,
                    awayTeam,
                    stadiumName,
                    matchDate
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009688)),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
        ) {
            Text("Confirm • $totalPrice EGP")
        }
    }

    // بعد نجاح حفظ الحجز → نروح للملخص
    val success = bookingViewModel.isSuccess.collectAsState().value
    LaunchedEffect(success) {
        if (success) {
            onNavigateToSummary()
        }
    }
}


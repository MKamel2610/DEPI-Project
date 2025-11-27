package com.example.ticketway.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketway.data.model.BookingItem
import com.example.ticketway.ui.ui.theme.*

@Composable
fun MyTicketCard(booking: BookingItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // Increased elevation
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // --- Match Header / Color Accent ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryGreen.copy(alpha = 0.9f))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "${booking.homeTeam} vs ${booking.awayTeam}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            // --- Details & Price ---
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {

                // Status Indicator
                Text(
                    text = "Status: ${booking.paymentStatus}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (booking.paymentStatus == "PAID") PrimaryGreen else MaterialTheme.colorScheme.error
                )

                Divider(color = LightGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))

                // --- Details Rows ---
                DetailRow(
                    icon = Icons.Default.LocationOn,
                    label = "Stadium:",
                    value = booking.stadiumName
                )
                DetailRow(
                    icon = Icons.Default.CalendarToday,
                    label = "Time:",
                    // In a real app, this should be formatted with the 7-day shift. Using raw data for now.
                    value = booking.matchDate
                )

                Spacer(modifier = Modifier.height(8.dp))

                // --- Tickets Summary & Price ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Tickets:", color = DarkText, fontWeight = FontWeight.Medium)
                        TicketDetailRow(label = "Total Seats:", value = "${booking.seatCount}")
                        if (booking.regularCount > 0) {
                            TicketDetailRow(label = "- Regular:", value = "${booking.regularCount}")
                        }
                        if (booking.premiumCount > 0) {
                            TicketDetailRow(label = "- Premium:", value = "${booking.premiumCount}")
                        }
                    }

                    // Total Price Box
                    Surface(
                        color = DarkText,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                "Paid:",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 10.sp
                            )
                            Text(
                                "EGP ${booking.totalPrice}.00",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(icon, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(18.dp))
        Text(label, fontSize = 14.sp, color = DarkText, fontWeight = FontWeight.Medium)
        Text(value, fontSize = 14.sp, color = LightText)
    }
}

@Composable
private fun TicketDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.padding(start = 18.dp), // Intentional indent
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 13.sp, color = DarkText.copy(alpha = 0.7f))
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = LightText)
    }
}
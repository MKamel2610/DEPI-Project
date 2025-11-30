package com.example.ticketway.ui.components.mytickets

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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.9f))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "${booking.homeTeam} vs ${booking.awayTeam}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            // --- Details & Price ---
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {

                // Status Indicator
                Text(
                    text = "Status: ${booking.paymentStatus}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (booking.paymentStatus == "PAID") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )

                Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp)) // LightGray -> surfaceVariant

                // --- Details Rows ---
                DetailRow(
                    icon = Icons.Default.LocationOn,
                    label = "Stadium:",
                    value = booking.stadiumName
                )
                DetailRow(
                    icon = Icons.Default.CalendarToday,
                    label = "Time:",
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
                        Text("Tickets:", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
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
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                "Paid:",
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                                fontSize = 10.sp
                            )
                            Text(
                                "EGP ${booking.totalPrice}.00",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.surface
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
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
        Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
        Text(value, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun TicketDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.padding(start = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
package com.example.ticketway.ui.screens.ticketdetails.previews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

// Data class to mirror the specific fields needed from BookingItem for the UI
data class TicketUIData(
    val bookingId: String,
    val homeTeam: String,
    val awayTeam: String,
    val stadiumName: String,
    val formattedDateTime: String,
    val regularCount: Int,
    val premiumCount: Int,
    val seatCount: Int,
    val totalPrice: Int,
    val paymentStatus: String
)

private data class ContentDetail(
    val icon: ImageVector,
    val label: String,
    val valueExtractor: (TicketUIData) -> String,
    val valueColor: ((TicketUIData) -> Color)? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailsContent(
    data: TicketUIData,
    onBack: () -> Unit,
    onViewQrCode: () -> Unit
) {
    val matchDetails = listOf(
        ContentDetail(
            icon = Icons.Default.SportsSoccer,
            label = "Match",
            valueExtractor = { "${it.homeTeam} vs ${it.awayTeam}" }
        ),
        ContentDetail(
            icon = Icons.Default.LocationOn,
            label = "Venue",
            valueExtractor = { it.stadiumName }
        ),
        ContentDetail(
            icon = Icons.Default.AccessTime,
            label = "Date & Time",
            valueExtractor = { it.formattedDateTime }
        ),
        ContentDetail(
            icon = Icons.Default.EmojiEvents,
            label = "Competition",
            valueExtractor = { "Football League (N/A)" }
        )
    )

    val ticketDetails = listOf(
        ContentDetail(
            icon = Icons.Default.ConfirmationNumber,
            label = "Tickets Count",
            valueExtractor = { "${it.seatCount} Total Tickets" }
        ),
        ContentDetail(
            icon = Icons.Default.AttachMoney,
            label = "Total Paid",
            valueExtractor = { "EGP ${String.format("%.2f", it.totalPrice.toDouble())}" }
        ),
        ContentDetail(
            icon = Icons.Default.Numbers,
            label = "Seat Section",
            valueExtractor = { "Section TBA" }
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ticket Details", color = MaterialTheme.colorScheme.onSurface) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            // Booking Reference Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Booking Reference:",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        data.bookingId.uppercase(Locale.ROOT),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Payment Status: ${data.paymentStatus}",
                        fontSize = 12.sp,
                        color = if (data.paymentStatus == "PENDING") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Match Details Card
            DetailsCard {
                matchDetails.forEachIndexed { index, detail ->
                    if (index > 0) {
                        Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)
                    }
                    DetailRow(
                        icon = detail.icon,
                        label = detail.label,
                        value = detail.valueExtractor(data),
                        valueColor = detail.valueColor?.invoke(data) ?: MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ticket Breakdown Card
            DetailsCard {
                Text(
                    "Ticket Breakdown",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                )
                Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)

                if (data.regularCount > 0) {
                    DetailRow(
                        icon = Icons.Default.Circle,
                        label = "Regular Tier",
                        value = "${data.regularCount} Tickets",
                        valueColor = MaterialTheme.colorScheme.onSurface
                    )
                }
                if (data.premiumCount > 0) {
                    if (data.regularCount > 0) Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)
                    DetailRow(
                        icon = Icons.Default.Star,
                        label = "Premium Tier",
                        value = "${data.premiumCount} Tickets",
                        valueColor = MaterialTheme.colorScheme.onSurface
                    )
                }

                Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)
                ticketDetails.forEach { detail ->
                    val color = if (detail.label == "Total Paid") {
                        MaterialTheme.colorScheme.primary
                    } else {
                        detail.valueColor?.invoke(data) ?: MaterialTheme.colorScheme.onSurface
                    }

                    DetailRow(
                        icon = detail.icon,
                        label = detail.label,
                        value = detail.valueExtractor(data),
                        valueColor = color
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // QR Code Button
            Button(
                onClick = onViewQrCode,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.QrCode2, contentDescription = "View QR Code", modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("View Ticket QR Code", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun DetailsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp), content = content)
    }
}

@Composable
fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Text(label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        }
        Text(
            value,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = valueColor,
            textAlign = TextAlign.End
        )
    }
}
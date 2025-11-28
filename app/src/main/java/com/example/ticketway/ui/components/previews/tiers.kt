package com.example.ticketway.ui.components.previews

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketway.ui.components.tierselectionscreen.TierOptionCard
import com.example.ticketway.ui.ui.theme.TicketWayTheme


@Preview(name = "Tier Card - No Selection", showBackground = true)
@Composable
private fun PreviewTierOptionCardEmpty() {
    TicketWayTheme {
        TierOptionCard(
            title = "Regular",
            price = 100,
            quantity = 0,
            onQuantityChange = {},
            canIncrease = true
        )
    }
}

@Preview(name = "Tier Card - With Selection", showBackground = true)
@Composable
private fun PreviewTierOptionCardSelected() {
    TicketWayTheme {
        TierOptionCard(
            title = "Premium",
            price = 200,
            quantity = 2,
            onQuantityChange = {},
            canIncrease = true
        )
    }
}

@Preview(name = "Tier Card - Max Reached", showBackground = true)
@Composable
private fun PreviewTierOptionCardMaxReached() {
    TicketWayTheme {
        TierOptionCard(
            title = "Regular",
            price = 100,
            quantity = 4,
            onQuantityChange = {},
            canIncrease = false
        )
    }
}


@Composable
private fun PreviewBookingBottomBar(
    totalPrice: Int = 0,
    totalTickets: Int = 0,
    onNavigateToSummary: () -> Unit = {}
) {
    val isBookingValid = totalTickets > 0 && totalTickets <= 4

    Surface(
        modifier = androidx.compose.ui.Modifier
            .fillMaxWidth(),
        color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        ) {
            // Price Display
            androidx.compose.foundation.layout.Column(
                modifier = androidx.compose.ui.Modifier.weight(1f)
            ) {
                androidx.compose.material3.Text(
                    text = "Total Price",
                    fontSize = 12.sp,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                )
                androidx.compose.material3.Text(
                    text = "EGP ${"%.2f".format(totalPrice.toDouble())}",
                    fontSize = 24.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                )
            }

            // Confirm Button
            androidx.compose.material3.Button(
                onClick = onNavigateToSummary,
                enabled = isBookingValid,
                modifier = androidx.compose.ui.Modifier
                    .width(180.dp)
                    .height(56.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary
                )
            ) {
                androidx.compose.material3.Text(
                    text = if (totalTickets == 0) "Select Tickets" else "Proceed to Payment",
                    fontSize = 14.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Preview(name = "Bottom Bar - Empty", showBackground = true)
@Composable
private fun PreviewBookingBottomBarEmpty() {
    TicketWayTheme {
        PreviewBookingBottomBar(
            totalPrice = 0,
            totalTickets = 0
        )
    }
}

@Preview(name = "Bottom Bar - With Tickets", showBackground = true)
@Composable
private fun PreviewBookingBottomBarWithTickets() {
    TicketWayTheme {
        PreviewBookingBottomBar(
            totalPrice = 400,
            totalTickets = 2
        )
    }
}

@Preview(name = "Bottom Bar - Max Tickets", showBackground = true)
@Composable
private fun PreviewBookingBottomBarMaxTickets() {
    TicketWayTheme {
        PreviewBookingBottomBar(
            totalPrice = 800,
            totalTickets = 4
        )
    }
}
package com.example.ticketway.ui.screens.tierselection.previews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import android.graphics.Color
import com.example.ticketway.ui.components.tierselectionscreen.TierOptionCard

// Data class to hold all necessary UI data for the Tier Selection Screen
data class TierSelectionUIData(
    val homeTeam: String,
    val awayTeam: String,
    val homeLogoUrl: String?,
    val awayLogoUrl: String?,
    val venueName: String,
    val formattedDateTime: String,
    val userName: String,
    val regCount: Int,
    val premCount: Int,
    val totalTickets: Int,
    val maxTickets: Int,
    val errorMsg: String?,
    val isLoading: Boolean
)

private data class ContentDetail(
    val icon: ImageVector,
    val label: String,
    val valueExtractor: (TierSelectionUIData) -> String,
    val valueColor: ((TierSelectionUIData) -> Color)? = null
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TierSelectionContent(
    data: TierSelectionUIData,
    totalPrice: Int,
    onBack: () -> Unit,
    onNavigateToSummary: () -> Unit,
    onQuantityChange: (String, Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Tickets", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            )
        },
        bottomBar = {
            BookingBottomBarContent(
                totalPrice = totalPrice,
                totalTickets = data.totalTickets,
                onNavigateToSummary = onNavigateToSummary,
                maxTickets = data.maxTickets
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) { padding ->

        if (data.homeTeam.isBlank() && data.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            MatchInfoCard(data)

            Spacer(modifier = Modifier.height(16.dp))

            LimitBanner(data.userName, data.totalTickets, data.maxTickets, data.errorMsg)

            if (data.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Select Seating Tier",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                TierOptionCard(
                    title = "Regular",
                    price = 100,
                    quantity = data.regCount,
                    onQuantityChange = { count -> onQuantityChange("Regular", count) },
                    canIncrease = data.totalTickets < data.maxTickets
                )

                TierOptionCard(
                    title = "Premium",
                    price = 200,
                    quantity = data.premCount,
                    onQuantityChange = { count -> onQuantityChange("Premium", count) },
                    canIncrease = data.totalTickets < data.maxTickets
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// NOTE: Duplicated/modified helper composables from the original screen are placed here.

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MatchInfoCard(data: TierSelectionUIData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TeamDisplay(data.homeTeam, data.homeLogoUrl, Alignment.Start)
                Text(
                    "vs",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TeamDisplay(data.awayTeam, data.awayLogoUrl, Alignment.End)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = "Venue", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text(data.venueName, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, contentDescription = "Date", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text(data.formattedDateTime, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun TeamDisplay(name: String, logoUrl: String?, alignment: Alignment.Horizontal) {
    Column(horizontalAlignment = alignment) {
        AsyncImage(model = logoUrl, contentDescription = name, modifier = Modifier.size(48.dp), contentScale = ContentScale.Fit)
        Spacer(modifier = Modifier.height(4.dp))
        Text(name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun LimitBanner(userName: String, totalTickets: Int, maxTickets: Int, errorMsg: String?) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = "User", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text("Welcome, $userName!", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(4.dp))

            val remaining = maxTickets - totalTickets
            Text("Selected: $totalTickets / $maxTickets tickets. ($remaining remaining)", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)

            if (errorMsg != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(errorMsg, fontSize = 13.sp, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// Replicating BookingBottomBar logic here to be stateless
@Composable
private fun BookingBottomBarContent(
    totalPrice: Int,
    totalTickets: Int,
    onNavigateToSummary: () -> Unit,
    maxTickets: Int
) {
    val isBookingValid = totalTickets > 0 && totalTickets <= maxTickets

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        color = MaterialTheme.colorScheme.surface,
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "EGP ${"%.2f".format(totalPrice.toDouble())}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
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
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
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


// ==================== Previews ====================

private val MockData = TierSelectionUIData(
    homeTeam = "Real Madrid",
    awayTeam = "Barcelona",
    homeLogoUrl = null,
    awayLogoUrl = null,
    venueName = "Santiago Bernabéu",
    formattedDateTime = "Jan 05, 2025 • 21:00",
    userName = "Alex",
    regCount = 1,
    premCount = 0,
    totalTickets = 1,
    maxTickets = 4,
    errorMsg = null,
    isLoading = false
)

private val MockDataFull = MockData.copy(
    regCount = 2,
    premCount = 2,
    totalTickets = 4,
    errorMsg = "Maximum ticket limit reached."
)

@RequiresApi(Build.VERSION_CODES.O)
@Preview(name = "Tier Selection - Normal", showBackground = true)
@Composable
private fun PreviewTierSelectionNormal() {
    MaterialTheme {
        TierSelectionContent(
            data = MockData,
            totalPrice = 100,
            onBack = {},
            onNavigateToSummary = {},
            onQuantityChange = { _, _ -> }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(name = "Tier Selection - Max Tickets", showBackground = true)
@Composable
private fun PreviewTierSelectionMax() {
    MaterialTheme {
        TierSelectionContent(
            data = MockDataFull,
            totalPrice = 600,
            onBack = {},
            onNavigateToSummary = {},
            onQuantityChange = { _, _ -> }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(name = "Tier Selection - Empty (Select button disabled)", showBackground = true)
@Composable
private fun PreviewTierSelectionEmpty() {
    MaterialTheme {
        TierSelectionContent(
            data = MockData.copy(regCount = 0, premCount = 0, totalTickets = 0, errorMsg = null),
            totalPrice = 0,
            onBack = {},
            onNavigateToSummary = {},
            onQuantityChange = { _, _ -> }
        )
    }
}
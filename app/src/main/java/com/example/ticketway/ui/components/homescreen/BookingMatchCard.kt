package com.example.ticketway.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons // NEW Import
import androidx.compose.material.icons.filled.LocationOn // NEW Import
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ticketway.data.network.model.fixtures.FixtureItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.ticketway.ui.ui.theme.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingMatchCard(
    fixture: FixtureItem,
    isAvailable: Boolean,
    onBookClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        tonalElevation = 1.dp,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.Top
        ) {
            // Main content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Logos on top - each end of the card
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Home team logo
                    AsyncImage(
                        model = fixture.teams.home.logo,
                        contentDescription = fixture.teams.home.name,
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Fit
                    )

                    // Away team logo
                    AsyncImage(
                        model = fixture.teams.away.logo,
                        contentDescription = fixture.teams.away.name,
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // Team names with "vs" - centered
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = fixture.teams.home.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Text(
                        text = " vs ",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = LightText,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Text(
                        text = fixture.teams.away.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }

                // Date and Time
                Text(
                    text = formatDateTime(fixture.fixture.date),
                    fontSize = 14.sp,
                    color = MediumText,
                    fontWeight = FontWeight.Medium
                )

                // Venue
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // FIXED: Replaced emoji with Icon
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Venue",
                        tint = DarkText, // Assuming DarkText is the desired tint color
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = fixture.fixture.venue?.name?.takeIf { it.isNotBlank() } ?: "Venue TBA",
                        fontSize = 13.sp,
                        color = LightText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // League info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // League logo
                    AsyncImage(
                        model = fixture.league.logo,
                        contentDescription = fixture.league.name,
                        modifier = Modifier.size(14.dp),
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        text = "${fixture.league.name} • ${fixture.league.country}",
                        fontSize = 11.sp,
                        color = LightText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Book button - matches card height exactly
            Surface(
                onClick = onBookClick,
                enabled = isAvailable,
                color = if (isAvailable) PrimaryGreen else Color.Gray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 8.dp,
                    bottomEnd = 8.dp,
                    bottomStart = 0.dp
                ),
                modifier = Modifier
                    .fillMaxHeight()
                    .width(85.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isAvailable) "Book" else "Sold\nOut",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDateTime(dateTimeString: String): String {
    return try {
        val dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME)
        // Shift date by +7 days
        val shiftedDateTime = dateTime.plusDays(7)
        shiftedDateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy • HH:mm"))
    } catch (e: Exception) {
        dateTimeString
    }
}
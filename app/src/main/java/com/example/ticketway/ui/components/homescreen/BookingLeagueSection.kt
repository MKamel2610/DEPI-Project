package com.example.ticketway.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ticketway.data.network.model.fixtures.FixtureItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.example.ticketway.ui.ui.theme.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingLeagueSection(
    fixtures: List<FixtureItem>,
    onMatchClick: (FixtureItem) -> Unit,
    onLeagueClick: () -> Unit = {}
) {
    if (fixtures.isEmpty()) return

    val league = fixtures.first().league

    // Sort fixtures: available first, then by date
    val sortedFixtures = fixtures.sortedWith(
        compareBy(
            { !isFixtureAvailable(it) }, // Available first
            { it.fixture.timestamp }      // Then by time
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        color = LightGray,
        tonalElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            // League Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onLeagueClick)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // League logo
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = Color.White
                    ) {
                        if (!league.logo.isNullOrEmpty()) {
                            AsyncImage(
                                model = league.logo,
                                contentDescription = league.name,
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(6.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = league.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = DarkText
                        )
                        Text(
                            text = league.country,
                            fontSize = 13.sp,
                            color = LightText
                        )
                    }
                }

                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "View all",
                    tint = LightText
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Matches
            sortedFixtures.forEach { fixture ->
                BookingMatchCard(
                    fixture = fixture,
                    isAvailable = isFixtureAvailable(fixture),
                    onBookClick = { onMatchClick(fixture) }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun isFixtureAvailable(fixture: FixtureItem): Boolean {
    return try {
        val fixtureDateTime = LocalDateTime.parse(
            fixture.fixture.date,
            DateTimeFormatter.ISO_DATE_TIME
        )
        val today = LocalDateTime.now()

        // Yesterday's matches are unavailable (sold out)
        // Today and tomorrow are available
        fixtureDateTime.toLocalDate() >= today.toLocalDate()
    } catch (e: Exception) {
        true // Default to available if can't parse
    }
}
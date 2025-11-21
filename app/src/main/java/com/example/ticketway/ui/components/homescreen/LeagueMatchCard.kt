package com.example.ticketway.ui.components.homescreen

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

//val PrimaryGreen = Color(0xFF009688)
//val LightGray = Color(0xFFF5F5F5)
//val DarkText = Color(0xFF212121)
//val LightText = Color(0xFF757575)

@Composable
fun LeagueMatchCard(
    fixtures: List<FixtureItem>,
    onMatchClick: (FixtureItem) -> Unit,
    onLeagueClick: () -> Unit = {}
) {
    if (fixtures.isEmpty()) return

    val league = fixtures.first().league

    // Sort fixtures by time (upcoming first, then by timestamp)
    val sortedFixtures = fixtures.sortedBy { fixture ->
        val isFinished = fixture.fixture.status.short == "FT"
        val timestamp = fixture.fixture.timestamp
        // Put finished matches at the end, sort rest by time
        if (isFinished) Long.MAX_VALUE else timestamp
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        color = LightGray,
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // League Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onLeagueClick),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // League logo - bigger and better aligned
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
                                    .padding(4.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Box(contentAlignment = Alignment.Center) {
                                // Fallback if no logo
                            }
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = league.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = DarkText
                        )
                        Text(
                            text = league.country,
                            fontSize = 12.sp,
                            color = LightText
                        )
                    }
                }

                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "More",
                    tint = LightText
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Matches (sorted by time)
            sortedFixtures.forEach { fixture ->
                MatchRow(
                    fixture = fixture,
                    onClick = { onMatchClick(fixture) }
                )
                if (fixture != sortedFixtures.last()) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
package com.example.ticketway.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun MatchRow(
    fixture: FixtureItem,
    onClick: () -> Unit
) {
    val isFinished = fixture.fixture.status.short == "FT"
    val isLive = fixture.fixture.status.short !in listOf("FT", "NS", "TBD", "PST", "CANC", "ABD", "AWD", "WO")
    val statusText = when {
        isFinished -> "FT"
        isLive -> "${fixture.fixture.status.elapsed ?: 0}'"
        else -> fixture.fixture.date.substring(11, 16) // Extract HH:mm
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status/Time
            Text(
                text = statusText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = when {
                    isFinished -> LightText
                    isLive -> PrimaryGreen
                    else -> DarkText
                },
                modifier = Modifier.width(45.dp)
            )

            // Teams
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Home Team
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TeamLogo(fixture.teams.home.logo)
                    Text(
                        text = fixture.teams.home.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkText,
                        modifier = Modifier.weight(1f)
                    )
                    if (isFinished) {
                        Text(
                            text = fixture.goals.home?.toString() ?: "-",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText,
                            modifier = Modifier.width(24.dp)
                        )
                    }
                }

                // Away Team
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TeamLogo(fixture.teams.away.logo)
                    Text(
                        text = fixture.teams.away.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkText,
                        modifier = Modifier.weight(1f)
                    )
                    if (isFinished) {
                        Text(
                            text = fixture.goals.away?.toString() ?: "-",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText,
                            modifier = Modifier.width(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TeamLogo(logoUrl: String?) {
    Surface(
        modifier = Modifier.size(28.dp),
        shape = CircleShape,
        color = Color.White
    ) {
        if (!logoUrl.isNullOrEmpty()) {
            AsyncImage(
                model = logoUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                // Fallback if no logo
            }
        }
    }
}
package com.example.ticketway.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketway.data.network.model.fixtures.FixtureItem

val PrimaryGreen = Color(0xFF009688)
val BorderGray = Color(0xFFE0E0E0)
val DarkText = Color(0xFF212121)

@Composable
fun LeagueFilterRow(
    fixtures: List<FixtureItem>,
    selectedLeagueId: Int?,
    onLeagueSelected: (Int?) -> Unit
) {
    val leagues = fixtures.map { it.league }.distinctBy { it.id }

    LazyRow(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // All filter
        item {
            FilterChip(
                name = "All",
                isSelected = selectedLeagueId == null,
                onClick = { onLeagueSelected(null) }
            )
        }

        // League filters
        items(leagues.size) { index ->
            val league = leagues[index]
            FilterChip(
                name = getLeagueShortName(league.name),
                isSelected = selectedLeagueId == league.id,
                onClick = { onLeagueSelected(league.id) }
            )
        }
    }
}

@Composable
fun FilterChip(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) PrimaryGreen else BorderGray,
                shape = RoundedCornerShape(24.dp)
            ),
        color = Color.White,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = name,
                color = if (isSelected) PrimaryGreen else DarkText,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}

fun getLeagueShortName(leagueName: String): String {
    return when {
        leagueName.contains("Premier League", ignoreCase = true) -> "EPL"
        leagueName.contains("La Liga", ignoreCase = true) -> "La Liga"
        leagueName.contains("Bundesliga", ignoreCase = true) -> "Bundesliga"
        leagueName.contains("Serie A", ignoreCase = true) -> "Serie A"
        leagueName.contains("Ligue 1", ignoreCase = true) -> "Ligue 1"
        leagueName.contains("Champions", ignoreCase = true) -> "UCL"
        leagueName.contains("World Cup", ignoreCase = true) -> "World Cup"
        leagueName.contains("Egyptian", ignoreCase = true) -> "Egyptian League"
        else -> leagueName.take(15)
    }
}
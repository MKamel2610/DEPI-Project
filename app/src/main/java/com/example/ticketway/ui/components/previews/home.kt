package com.example.ticketway.ui.components.previews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.ticketway.data.network.model.common.League
import com.example.ticketway.ui.components.homescreen.BookingLeagueSection
import com.example.ticketway.ui.components.homescreen.BookingMatchCard
import com.example.ticketway.ui.components.homescreen.BottomNavigationBar
import com.example.ticketway.ui.components.homescreen.LeagueFilterRow
import com.example.ticketway.ui.components.homescreen.mock.mockFixtureDetails
import com.example.ticketway.ui.components.homescreen.mock.mockLeague
import com.example.ticketway.ui.components.homescreen.mock.mockTeams
import com.example.ticketway.ui.components.homescreen.mock.updatedMockFixtureItem
import com.example.ticketway.ui.theme.TicketWayTheme

val mockLeague2 = League(
    id = 39,
    name = "Premier League",
    country = "England",
    logo = "url/epl_logo.png",
    flag = "url/england_flag.png",
    season = 2025,
    round = "Regular Season - 14"
)

val mockFixtureItem2 = updatedMockFixtureItem.copy(
    fixture = mockFixtureDetails.copy(id = 123457),
    league = mockLeague2
)

// List for LeagueFilterRow and BookingLeagueSection previews
val mockFixturesList = listOf(
    updatedMockFixtureItem,
    mockFixtureItem2,
    updatedMockFixtureItem.copy(fixture = mockFixtureDetails.copy(id = 123458, date = "2025-11-27T20:00:00+00:00")),
    mockFixtureItem2.copy(fixture = mockFixtureDetails.copy(id = 123459, date = "2025-11-28T20:00:00+00:00")),
)

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewBookingMatchCardAvailable() {
    TicketWayTheme {
        BookingMatchCard(
            fixture = updatedMockFixtureItem,
            isAvailable = true,
            onBookClick = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewBookingMatchCardSoldOut() {
    TicketWayTheme {
        BookingMatchCard(
            fixture = updatedMockFixtureItem.copy(teams = mockTeams.copy(home = mockTeams.home.copy(name = "Long Name Team"))),
            isAvailable = false,
            onBookClick = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewBookingLeagueSection() {
    TicketWayTheme {
        BookingLeagueSection(
            fixtures = mockFixturesList.filter { it.league.id == mockLeague.id },
            onMatchClick = {}
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewLeagueFilterRowAllSelected() {
    TicketWayTheme {
        LeagueFilterRow(
            fixtures = mockFixturesList,
            selectedLeagueId = null,
            onLeagueSelected = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewLeagueFilterRowLeagueSelected() {
    TicketWayTheme {
        LeagueFilterRow(
            fixtures = mockFixturesList,
            selectedLeagueId = mockLeague2.id,
            onLeagueSelected = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewBottomNavigationBarHome() {
    TicketWayTheme {
        BottomNavigationBar(
            selectedTab = "home",
            onTabSelected = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewBottomNavigationBarTickets() {
    TicketWayTheme {
        BottomNavigationBar(
            selectedTab = "My Tickets",
            onTabSelected = {}
        )
    }
}
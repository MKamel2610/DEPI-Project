package com.example.ticketway.ui.components.homescreen.mock

import com.example.ticketway.data.network.model.common.Team
import com.example.ticketway.data.network.model.common.League
import com.example.ticketway.data.network.model.common.Venue
import com.example.ticketway.data.network.model.common.Status
import com.example.ticketway.data.network.model.common.Goals
import com.example.ticketway.data.network.model.common.Score
import com.example.ticketway.data.network.model.fixtures.FixtureDetails
import com.example.ticketway.data.network.model.fixtures.FixtureItem
import com.example.ticketway.data.network.model.fixtures.Periods
import com.example.ticketway.data.network.model.fixtures.Teams


val mockVenue = Venue(
    id = 555,
    name = "Santiago Bernabéu",
    city = "Madrid"
)

// Based on Status.kt
val mockStatus = Status(
    long = "Match Finished",
    short = "FT",
    elapsed = 90,
    extra = null
)

val mockGoalsFinal = Goals(
    home = 3,
    away = 2
)

val mockTeamHome = Team(
    id = 541,
    name = "Real Madrid",
    logo = "url/realmadrid_logo.png",
    winner = true
)

val mockTeamAway = Team(
    id = 530,
    name = "FC Barcelona",
    logo = "url/barcelona_logo.png",
    winner = false
)

val mockLeague = League(
    id = 140,
    name = "La Liga",
    country = "Spain",
    logo = "url/laliga_logo.png",
    flag = "url/spain_flag.png",
    season = 2025,
    round = "Regular Season - 14"
)

val mockScore = Score(
    halftime = Goals(home = 1, away = 1),
    fulltime = mockGoalsFinal,
    extratime = null,
    penalty = null
)


val mockPeriods = Periods(
    first = 1732137600L,
    second = 1732141200L
)

val mockFixtureDetails = FixtureDetails(
    id = 123456, // Int
    referee = "Antonio Mateu Lahoz",
    timezone = "UTC",
    date = "2025-11-20T20:00:00+00:00",
    timestamp = 1732137600L,
    periods = mockPeriods,
    venue = mockVenue,
    status = mockStatus
)

val mockTeams = Teams(
    home = mockTeamHome,
    away = mockTeamAway
)


val updatedMockFixtureItem = FixtureItem(
    fixture = mockFixtureDetails,
    league = mockLeague,
    teams = mockTeams,
    goals = mockGoalsFinal,
    score = mockScore
)
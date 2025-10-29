package com.example.ticketway.data.mappers

import com.example.ticketway.data.local.entities.FixtureEntity
import com.example.ticketway.data.network.model.common.*
import com.example.ticketway.data.network.model.fixtures.*

fun FixtureItem.toEntity(date: String, source: String): FixtureEntity {
    return FixtureEntity(
        id = fixture.id,
        date = date,
        leagueId = league.id,
        leagueName = league.name,
        leagueLogo = league.logo,
        homeTeamId = teams.home.id,
        homeTeamName = teams.home.name,
        homeTeamLogo = teams.home.logo,
        awayTeamId = teams.away.id,
        awayTeamName = teams.away.name,
        awayTeamLogo = teams.away.logo,
        goalsHome = goals.home,
        goalsAway = goals.away,
        matchStatus = fixture.status.short,
        matchTime = fixture.date,
        venue = fixture.venue?.name,
        referee = fixture.referee,
        cachedAt = System.currentTimeMillis(),
        source = source
    )
}

fun mapFixturesFromEntity(entities: List<FixtureEntity>): FixtureResponse {
    val items = entities.map { entity ->
        FixtureItem(
            fixture = FixtureDetails(
                id = entity.id,
                referee = entity.referee,
                timezone = "UTC",
                date = entity.matchTime ?: "",
                timestamp = 0L,
                periods = null,
                venue = entity.venue?.let { Venue(null, it, null) },
                status = Status(long = null, short = entity.matchStatus, elapsed = null, extra = null)
            ),
            league = League(
                id = entity.leagueId,
                name = entity.leagueName,
                country = "",
                logo = entity.leagueLogo ?: "",
                flag = null,
                season = 0,
                round = ""
            ),
            teams = Teams(
                home = Team(entity.homeTeamId, entity.homeTeamName, entity.homeTeamLogo ?: "", null),
                away = Team(entity.awayTeamId, entity.awayTeamName, entity.awayTeamLogo ?: "", null)
            ),
            goals = Goals(home = entity.goalsHome, away = entity.goalsAway),
            score = Score(null, null, null, null)
        )
    }
    return FixtureResponse(items)
}

package com.example.ticketway.data.network

import com.example.ticketway.data.network.model.fixtures.FixtureResponse
import com.example.ticketway.data.network.model.squads.SquadResponse
import com.example.ticketway.data.network.model.standings.StandingsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("fixtures")
    suspend fun getFixtures(
        @Query("date") date: String
    ): Response<FixtureResponse>

    @GET("standings")
    suspend fun getStandings(
        @Query("league") leagueId: Int,
        @Query("season") season: Int
    ): Response<StandingsResponse>

    @GET("players/squads")
    suspend fun getSquad(
        @Query("team") teamId: Int
    ): Response<SquadResponse>
}

package com.example.ticketway.data.repository

import com.example.ticketway.data.local.CacheDatabase
import com.example.ticketway.data.local.entities.SquadEntity
import com.example.ticketway.data.mappers.mapFixturesFromEntity
import com.example.ticketway.data.mappers.toEntity
import com.example.ticketway.data.mappers.toModel
import com.example.ticketway.data.network.RetrofitInstance
import com.example.ticketway.data.network.model.fixtures.FixtureResponse
import com.example.ticketway.data.network.model.squads.SquadResponse
import com.example.ticketway.data.network.model.standings.StandingsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FootballRepository(private val db: CacheDatabase) {

    private val fixturesDao = db.fixturesDao()
    private val standingsDao = db.standingsDao()
    private val squadsDao = db.squadsDao()

    companion object {
        private const val CACHE_EXPIRATION = 3 * 60 * 60 * 1000 // 3 hours
    }

    // -----------------------------
    // FIXTURES
    // -----------------------------
    suspend fun getFixtures(date: String): FixtureResponse? = withContext(Dispatchers.IO) {
        val cached = fixturesDao.getFixturesByDate(date)
        if (cached.isNotEmpty() && !isCacheExpired(cached.first().cachedAt)) {
            return@withContext mapFixturesFromEntity(cached)
        }

        val response = RetrofitInstance.api.getFixtures(date)
        response.body()?.response?.let { fixtures ->
            val entities = fixtures.map { it.toEntity(date, "api") }
            fixturesDao.insertFixtures(entities)
        }
        return@withContext response.body()
    }

    // -----------------------------
    // STANDINGS
    // -----------------------------
    suspend fun getStandings(leagueId: Int, season: Int): StandingsResponse? =
        withContext(Dispatchers.IO) {
            val cached = standingsDao.getStandings(leagueId, season)
            if (cached.isNotEmpty() && !isCacheExpired(cached.first().cachedAt)) {
                return@withContext cached.first().toModel()
            }

            val response = RetrofitInstance.api.getStandings(leagueId, season)
            response.body()?.let { body ->
                val entity = body.toEntity(leagueId, season)
                standingsDao.insertStandings(listOf(entity))
            }
            return@withContext response.body()
        }

    // -----------------------------
    // CACHE VALIDATION
    // -----------------------------
    private fun isCacheExpired(cachedAt: Long): Boolean {
        return System.currentTimeMillis() - cachedAt > CACHE_EXPIRATION
    }

    // -----------------------------
    // SQUAD
    // -----------------------------
    suspend fun getSquad(teamId: Int): SquadResponse? = withContext(Dispatchers.IO) {
        val cached = squadsDao.getSquadByTeamId(teamId)
        if (cached != null && !isCacheExpired(cached)) {
            return@withContext cached.toModel()
        }

        val response = RetrofitInstance.api.getSquad(teamId)
        response.body()?.let { body ->
            val entity = body.toEntity(teamId)
            squadsDao.insertSquad(entity)
        }
        return@withContext response.body()
    }

    private fun isCacheExpired(entity: SquadEntity): Boolean {
        // Since SquadEntity doesn't store timestamp yet, skip expiration for now
        return false
    }
}

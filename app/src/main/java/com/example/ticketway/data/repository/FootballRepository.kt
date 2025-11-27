package com.example.ticketway.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.ZoneId // NEW IMPORT
import java.time.ZonedDateTime // NEW IMPORT

class FootballRepository(private val db: CacheDatabase) {

    private val fixturesDao = db.fixturesDao()
    private val standingsDao = db.standingsDao()
    private val squadsDao = db.squadsDao()

    companion object {
        private const val CACHE_EXPIRATION = 15 * 60 * 1000 // 15 minutes

        // Our selected leagues for booking
        private val SUPPORTED_LEAGUES = listOf(
            860, // ? League
            12,  // ? League
            20,  // ? League
            233, // Egyptian Premier League
            539, // ? League
            714, // ? League
            887, // ? League
            888, // ? League
            889, // ? League
            895, // ? League
            301, // ? League
            303, // ? League
            302, // ? League
            307, // ? League
            308, // ? League
            309, // ? League
            827, // ? League
            19,  // ? League
            6    // ? League
        )
    }

    // -----------------------------
    // FETCH 3-DAY WINDOW FIXTURES
    // -----------------------------
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getThreeDayFixtures(): FixtureResponse? = withContext(Dispatchers.IO) {

        // FIX: Use UTC time zone to determine the current calendar day for API consistency
        val utcZone = ZoneId.of("UTC")
        val today = ZonedDateTime.now(utcZone).toLocalDate()

        val yesterday = today.minusDays(1)
        val tomorrow = today.plusDays(1)

        val dates = listOf(yesterday, today, tomorrow)
        val allFixtures = mutableListOf<com.example.ticketway.data.network.model.fixtures.FixtureItem>()

        Log.d("FootballRepository", "📅 Fetching 3-day window (UTC): $yesterday, $today, $tomorrow")

        dates.forEach { date ->
            val dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            // Check cache first
            val cached = fixturesDao.getFixturesByDate(dateStr)
            if (cached.isNotEmpty() && !isCacheExpired(cached.first().cachedAt)) {
                Log.d("FootballRepository", "✅ Cache hit for $dateStr: ${cached.size} fixtures")
                val fixtureResponse = mapFixturesFromEntity(cached)
                fixtureResponse?.response?.let { allFixtures.addAll(it) }
            } else {
                // Fetch from API
                Log.d("FootballRepository", "📡 Fetching from API: $dateStr")
                try {
                    val response = RetrofitInstance.api.getFixtures(dateStr)
                    response.body()?.response?.let { fixtures ->
                        // Filter to supported leagues
                        val filteredFixtures = fixtures.filter { it.league.id in SUPPORTED_LEAGUES }

                        // Log what we got
                        Log.d("FootballRepository", "📊 Date $dateStr: ${fixtures.size} total, ${filteredFixtures.size} filtered")
                        filteredFixtures.groupBy { it.league.id to it.league.name }.forEach { (league, matches) ->
                            Log.d("FootballRepository", "   League ${league.first} (${league.second}): ${matches.size} matches")
                        }

                        if (filteredFixtures.isNotEmpty()) {
                            // Cache the fixtures
                            val entities = filteredFixtures.map { it.toEntity(dateStr, "api") }
                            fixturesDao.clearFixturesByDate(dateStr)
                            fixturesDao.insertFixtures(entities)

                            allFixtures.addAll(filteredFixtures)
                            Log.d("FootballRepository", "✅ Cached ${filteredFixtures.size} fixtures for $dateStr")
                        } else {
                            Log.w("FootballRepository", "⚠️ No matches found for our leagues on $dateStr")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("FootballRepository", "❌ Error fetching $dateStr: ${e.message}", e)
                }
            }
        }

        Log.d("FootballRepository", "✅ Total fixtures loaded: ${allFixtures.size}")
        return@withContext FixtureResponse(allFixtures)
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
    // SQUAD
    // -----------------------------
    suspend fun getSquad(teamId: Int): SquadResponse? = withContext(Dispatchers.IO) {
        val cached = squadsDao.getSquadByTeamId(teamId)
        if (cached != null) {
            return@withContext cached.toModel()
        }

        val response = RetrofitInstance.api.getSquad(teamId)
        response.body()?.let { body ->
            val entity = body.toEntity(teamId)
            squadsDao.insertSquad(entity)
        }
        return@withContext response.body()
    }

    // -----------------------------
    // CACHE VALIDATION
    // -----------------------------
    private fun isCacheExpired(cachedAt: Long): Boolean {
        return System.currentTimeMillis() - cachedAt > CACHE_EXPIRATION
    }
}
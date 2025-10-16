package com.example.ticketway.data.repository

import com.example.ticketway.data.network.RetrofitInstance
import retrofit2.Response

class FixturesRepository {

    suspend fun getFixtures(date: String): Response<Any> {
        return RetrofitInstance.api.getFixtures(date)
    }
}

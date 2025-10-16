package com.example.ticketway.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("fixtures")
    suspend fun getFixtures(
        @Query("date") date: String
    ): Response<Any> // keep it as Any for now
}

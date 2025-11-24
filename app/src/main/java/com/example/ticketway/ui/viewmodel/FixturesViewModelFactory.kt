package com.example.ticketway.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ticketway.data.local.CacheDatabase
import com.example.ticketway.data.repository.FootballRepository

class FixturesViewModelFactory(
    private val db: CacheDatabase
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = FootballRepository(db)
        return FixturesViewModel(repository) as T
    }
}
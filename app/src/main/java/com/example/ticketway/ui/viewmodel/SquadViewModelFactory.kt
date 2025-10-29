package com.example.ticketway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ticketway.data.local.CacheDatabase
import com.example.ticketway.data.repository.FootballRepository

class SquadViewModelFactory(private val db: CacheDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = FootballRepository(db)
        return SquadViewModel(repository) as T
    }
}

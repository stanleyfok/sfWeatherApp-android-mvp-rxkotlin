package com.example.sfweather.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sfweather.models.SearchHistory

@Database(entities = [(SearchHistory::class)], version = 1, exportSchema = false)
abstract class AppDB: RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDAO

    companion object {
        const val DB_NAME = "WeatherDB"
    }
}
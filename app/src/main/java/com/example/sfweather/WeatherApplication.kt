package com.example.sfweather

import android.app.Application
import androidx.room.Room
import com.example.sfweather.common.databases.AppDB
import com.example.sfweather.common.repositories.SearchHistoryRepository
import com.example.sfweather.features.weatherDetails.services.OWService
import com.example.sfweather.common.services.SearchHistoryService
import com.example.sfweather.features.weatherDetails.repositories.OWRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class WeatherApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        val appModule = module {
            single {
                Room.databaseBuilder(
                    applicationContext,
                    AppDB::class.java,
                    AppDB.DB_NAME
                ).build()
            }
            single { get<AppDB>().searchHistoryDao() }
            single { SearchHistoryService() }
            single { SearchHistoryRepository() }
            single { OWService() }
            single { OWRepository.create() }
        }

        startKoin {
            androidContext(this@WeatherApplication)
            modules(appModule)
        }
    }
}

package com.example.sfweather

import android.app.Application
import androidx.room.Room
import com.example.sfweather.databases.AppDB
import com.example.sfweather.repositories.SearchHistoryRepository
import com.example.sfweather.services.WeatherService
import com.example.sfweather.repositories.OWRepository
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
            single { WeatherService() }
            single { SearchHistoryRepository() }
            single { OWRepository.create() }
        }

        startKoin {
            androidContext(this@WeatherApplication)
            modules(appModule)
        }
    }
}

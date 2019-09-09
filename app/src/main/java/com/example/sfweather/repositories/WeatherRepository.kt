package com.example.sfweather.repositories

import com.example.sfweather.databases.SearchHistoryDAO
import com.example.sfweather.models.SearchHistory
import com.example.sfweather.models.OWResult
import com.example.sfweather.services.OWService
import io.reactivex.Completable
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

class WeatherRepository():KoinComponent {
    private val owService: OWService by inject()
    private val searchHistorydDao: SearchHistoryDAO by inject()

    fun findWeatherByCityName(cityName: String): Single<OWResult> {
        return this.owService.findByCityName(cityName)
    }

    fun findWeatherByCityId(cityId: Int): Single<OWResult> {
        return this.owService.findByCityId(cityId)
    }

    fun getAllHistories():Single<List<SearchHistory>> {
        return searchHistorydDao.getAll()
    }

    fun getLatestHistory(): Single<SearchHistory> {
        return searchHistorydDao.getLatest()
    }

    fun insertHistory(searchHistory: SearchHistory):Completable {
        return searchHistorydDao.getCountByCityId(searchHistory.cityId)
            .flatMapCompletable {
                if (it > 0) {
                    searchHistorydDao.update(searchHistory)
                } else {
                    searchHistorydDao.insert(searchHistory)
                }
            }
    }

    fun deleteHistoryByCityId(cityId: Int):Single<Int> {
        return searchHistorydDao.deleteByCityId(cityId)
    }
}
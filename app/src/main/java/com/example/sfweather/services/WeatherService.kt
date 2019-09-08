package com.example.sfweather.services

import com.example.sfweather.models.SearchHistory
import com.example.sfweather.repositories.SearchHistoryRepository
import com.example.sfweather.models.OWResult
import com.example.sfweather.repositories.OWRepository
import io.reactivex.Completable
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

class WeatherService():KoinComponent {
    private val owRepository: OWRepository by inject()
    private val searchHistoryRepository: SearchHistoryRepository by inject()

    fun findWeatherByCityName(cityName: String): Single<OWResult> {
        return this.owRepository.findByCityName(cityName)
    }

    fun findWeatherByCityId(cityId: Int): Single<OWResult> {
        return this.owRepository.findByCityId(cityId)
    }

    fun getAll():Single<List<SearchHistory>> {
        return searchHistoryRepository.getAllHistory()
    }

    fun getLatest(): Single<SearchHistory> {
        return searchHistoryRepository.getLatestHistory()
    }

    fun upsert(searchHistory: SearchHistory):Completable {
        return searchHistoryRepository.insertHistory(searchHistory)
    }

    fun deleteByCityId(cityId: Int):Single<Int> {
        return searchHistoryRepository.deleteHistoryByCityId(cityId)
    }
}
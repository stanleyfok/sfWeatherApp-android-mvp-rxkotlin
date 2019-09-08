package com.example.sfweather.common.services

import com.example.sfweather.common.models.SearchHistory
import com.example.sfweather.common.repositories.SearchHistoryRepository
import io.reactivex.Completable
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

class SearchHistoryService():KoinComponent {
    private val searchHistoryRepository: SearchHistoryRepository by inject()

    fun getAll():Single<List<SearchHistory>> {
        return searchHistoryRepository.getAll()
    }

    fun getLatest(): Single<SearchHistory> {
        return searchHistoryRepository.getLatest()
    }

    fun upsert(searchHistory: SearchHistory):Completable {
        return searchHistoryRepository.upsert(searchHistory)
    }

    fun deleteByCityId(cityId: Int):Single<Int> {
        return searchHistoryRepository.deleteByCityId(cityId)
    }
}
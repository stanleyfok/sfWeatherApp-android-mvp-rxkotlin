package com.example.sfweather.repositories

import com.example.sfweather.databases.SearchHistoryDAO
import com.example.sfweather.models.SearchHistory
import io.reactivex.Completable
import io.reactivex.Single
import org.koin.core.inject
import org.koin.core.KoinComponent

class SearchHistoryRepository: KoinComponent {

    private val dao: SearchHistoryDAO by inject()

    fun getAllHistory(): Single<List<SearchHistory>> {
        return dao.getAll()
    }

    fun getLatestHistory(): Single<SearchHistory> {
        return dao.getLatest()
    }

    fun insertHistory(searchHistory: SearchHistory):Completable {
        return dao.getCountByCityId(searchHistory.cityId)
            .flatMapCompletable {
                if (it > 0) {
                    dao.update(searchHistory)
                } else {
                    dao.insert(searchHistory)
                }
            }
    }

    fun deleteHistoryByCityId(cityId: Int):Single<Int> {
        return dao.deleteByCityId(cityId)
    }
}
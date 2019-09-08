package com.example.sfweather.common.repositories

import com.example.sfweather.common.databases.SearchHistoryDAO
import com.example.sfweather.common.models.SearchHistory
import io.reactivex.Completable
import io.reactivex.Single
import org.koin.core.inject
import org.koin.core.KoinComponent

class SearchHistoryRepository: KoinComponent {

    private val dao: SearchHistoryDAO by inject()

    fun getAll(): Single<List<SearchHistory>> {
        return dao.getAll()
    }

    fun getLatest(): Single<SearchHistory> {
        return dao.getLatest()
    }

    fun upsert(searchHistory: SearchHistory):Completable {
        return dao.getCountByCityId(searchHistory.cityId)
            .flatMapCompletable {
                if (it > 0) {
                    dao.update(searchHistory)
                } else {
                    dao.insert(searchHistory)
                }
            }
    }

    fun deleteByCityId(cityId: Int):Single<Int> {
        return dao.deleteByCityId(cityId)
    }
}
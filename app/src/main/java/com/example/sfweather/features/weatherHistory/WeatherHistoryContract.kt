package com.example.sfweather.features.weatherHistory

import com.example.sfweather.base.BasePresenter
import com.example.sfweather.common.models.SearchHistory

interface WeatherHistoryContract {
    interface View {
        fun reloadRecyclerView()
        fun onSelectSearchHistory(searchHistory: SearchHistory)
    }

    interface Presenter:BasePresenter<View> {
        var isEdit:Boolean
        fun onViewCreated()
        fun getSearchHistoryAtPosition(position: Int): SearchHistory?
        fun getSearchHistoryCount():Int
        fun selectSearchHistoryAtPosition(position: Int)
        fun removeSearchHistoryAtPosition(position: Int)
    }
}
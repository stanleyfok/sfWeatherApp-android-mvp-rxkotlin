package com.example.sfweather.features.weatherHistory

import com.example.sfweather.models.SearchHistory
import com.example.sfweather.services.WeatherService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

class WeatherHistoryPresenter: WeatherHistoryContract.Presenter, KoinComponent {
    private var view:WeatherHistoryContract.View? = null
    private val weatherService: WeatherService by inject()

    private var compositeDisposable:CompositeDisposable = CompositeDisposable()
    private var searchHistories: MutableList<SearchHistory>? = null

    override var isEdit:Boolean = false

    override fun attachView(view: WeatherHistoryContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.compositeDisposable.clear()
        this.view = null
    }

    override fun onViewCreated() {
        val disposable = weatherService.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(  // named arguments for lambda Subscribers
                onSuccess = {
                    this.searchHistories = it.toMutableList()
                    view?.reloadRecyclerView()
                }
            )

        disposable.addTo(compositeDisposable)
    }

    override fun getSearchHistoryAtPosition(position: Int): SearchHistory? {
        this.searchHistories?.let {
            if (position < it.size) {
                return it[position]
            }
        }

        return null
    }

    override fun getSearchHistoryCount():Int {
        this.searchHistories?.let {
            return it.size
        }

        return 0
    }

    override fun selectSearchHistoryAtPosition(position: Int) {
        val searchHistory = this.getSearchHistoryAtPosition(position)

        if (searchHistory != null) {
            this.view?.onSelectSearchHistory(searchHistory)
        }
    }

    override fun removeSearchHistoryAtPosition(position: Int) {
        val searchHistory = getSearchHistoryAtPosition(position)

        if (searchHistory != null) {
            val disposable = weatherService.deleteByCityId(searchHistory.cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(  // named arguments for lambda Subscribers
                    onSuccess = {
                        if (it > 0) {
                            searchHistories!!.removeAt(position)
                            view?.reloadRecyclerView()
                        }
                    }
                )

            disposable.addTo(compositeDisposable)
        }
    }
    //endregion
}
package com.example.sfweather.features.weatherDetails

import com.example.sfweather.models.OWApiError
import com.example.sfweather.models.OWResult
import com.example.sfweather.models.SearchHistory
import com.example.sfweather.repositories.WeatherRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.HttpException

class WeatherDetailsPresenter: KoinComponent, WeatherDetailsContract.Presenter {
    private var view: WeatherDetailsContract.View? = null
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val weatherRepository: WeatherRepository by inject()

    override fun attachView(view: WeatherDetailsContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.compositeDisposable.clear()
        this.view = null
    }

    override fun fetchLastStoredWeather() {
        val observable = weatherRepository.getLatestHistory()
            .flatMap{ searchHistory ->
                weatherRepository.findWeatherByCityId(searchHistory.cityId)
            }

        this.subscribeObservable(observable)
    }

    override fun fetchWeatherByCityName(cityName: String) {
        this.subscribeObservable(weatherRepository.findWeatherByCityName(cityName))
    }

    override fun fetchWeatherByCityId(cityId: Int) {
        this.subscribeObservable(weatherRepository.findWeatherByCityId(cityId))
    }
    //endregion

    //region private methods
    private fun subscribeObservable(observable: Single<OWResult>) {
        val disposable = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe{
                view?.setIsLoading(true)
            }
            .doAfterSuccess {
                insertSearchHistory(it)
            }
            .doFinally {
                view?.setIsLoading(false)
            }
            .subscribeBy(  // named arguments for lambda Subscribers
                onSuccess = {
                    handleSuccess(it)
                },
                onError =  {
                    hanleFailure(it)
                }
            )

        disposable.addTo(compositeDisposable)
    }

    private fun handleSuccess(owResult: OWResult) {
        val weatherDetailsData = WeatherDetailsData(owResult)
        view?.updateView(weatherDetailsData)
    }

    private fun hanleFailure(e: Throwable) {
        when (e) {
            is HttpException -> {
                val apiError = e.response()?.errorBody()?.let { OWApiError.createFromResponse(it) }

                apiError?.message?.let {
                    view?.showErrorMessage(it)
                } ?: run {
                    view?.showErrorMessage("Unknown Error")
                }
            }
            else -> {
                e.message?.let {
                    view?.showErrorMessage(it)
                } ?: run {
                    view?.showErrorMessage("Unknown Error")
                }
            }
        }

        view?.setIsLoading(false)
    }

    private fun insertSearchHistory(owResult: OWResult) {
        val timestamp = System.currentTimeMillis() / 1000;
        val searchHistory = SearchHistory(owResult.id, owResult.name, timestamp)

        weatherRepository.insertHistory(searchHistory)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
    //endregion
}
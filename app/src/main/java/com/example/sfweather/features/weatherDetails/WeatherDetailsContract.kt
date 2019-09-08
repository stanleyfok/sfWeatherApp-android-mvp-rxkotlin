package com.example.sfweather.features.weatherDetails

import com.example.sfweather.base.BasePresenter

interface WeatherDetailsContract {
    interface View {
        fun setIsLoading(bool: Boolean)
        fun updateView(data: WeatherDetailsData)
        fun showErrorMessage(errorMessage: String)
    }

    interface Presenter: BasePresenter<View> {
        fun fetchLastStoredWeather()
        fun fetchWeatherByCityName(cityName: String)
        fun fetchWeatherByCityId(cityId: Int)
    }
}
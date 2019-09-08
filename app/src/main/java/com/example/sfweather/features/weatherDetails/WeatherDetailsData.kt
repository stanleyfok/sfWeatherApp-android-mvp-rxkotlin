package com.example.sfweather.features.weatherDetails

import com.example.sfweather.models.OWResult

data class WeatherDetailsData(val owResult: OWResult) {
    var cityName: String = ""
    var temperature: Float
    var weatherDesc: String = ""

    init {
        this.cityName = owResult.name
        this.temperature = owResult.main.temp
        this.weatherDesc = owResult.weather[0].main
    }
}
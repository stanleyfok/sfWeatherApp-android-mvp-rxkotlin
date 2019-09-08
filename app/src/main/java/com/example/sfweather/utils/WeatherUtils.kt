package com.example.sfweather.utils

class WeatherUtils {
    companion object {
        fun kelvinToCelsius(tempInKelvin: Float): Float {
            return tempInKelvin - 273.15f;
        }
    }
}
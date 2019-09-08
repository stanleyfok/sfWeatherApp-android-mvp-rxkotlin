package com.example.sfweather.models

import com.google.gson.annotations.SerializedName

data class OWResult(
    @SerializedName("cod")
    var status: Int,

    @SerializedName("id")
    var id: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("timezone")
    var timezone: Int,

    @SerializedName("base")
    var base: String,

    @SerializedName("visibility")
    var visibility: Float,

    @SerializedName("dt")
    var datetime: Int,

    @SerializedName("coord")
    var coord: OWCoord,

    @SerializedName("weather")
    var weather: List<OWWeather>,

    @SerializedName("main")
    var main: OWMain,

    @SerializedName("wind")
    var wind: OWWind,

    @SerializedName("clouds")
    var clouds: OWClouds,

    @SerializedName("sys")
    var sys: OWSys
) {
    data class OWCoord(
        @SerializedName("lon")
        var lon: Float,

        @SerializedName("lat")
        var lat: Float
    )

    data class OWWeather(
        @SerializedName("id")
        var id: Int,

        @SerializedName("main")
        var main: String,

        @SerializedName("description")
        var description: String,

        @SerializedName("icon")
        var icon: String
    )

    data class OWMain(
        @SerializedName("temp")
        var temp: Float,

        @SerializedName("pressure")
        var pressure: Float,

        @SerializedName("humidity")
        var humidity: Float,

        @SerializedName("temp_min")
        var temp_min: Float,

        @SerializedName("temp_max")
        var temp_max: Float
    )

    data class OWWind(
        @SerializedName("speed")
        var speed: Float,

        @SerializedName("deg")
        var deg: Float
    )

    data class OWClouds(
        @SerializedName("all")
        var all: Float
    )

    data class OWSys(
        @SerializedName("type")
        var type: Int,

        @SerializedName("id")
        var id: Int,

        @SerializedName("message")
        var message: Float,

        @SerializedName("country")
        var country: String,

        @SerializedName("sunrise")
        var sunrise: Int,

        @SerializedName("sunset")
        var sunset: Int
    )
}

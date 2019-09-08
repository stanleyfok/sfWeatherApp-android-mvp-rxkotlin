package com.example.sfweather.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistory (
    @PrimaryKey
    var cityId: Int,

    @ColumnInfo(name = "cityName")
    var cityName: String,

    @ColumnInfo(name = "timestamp")
    var timestamp: Long
)

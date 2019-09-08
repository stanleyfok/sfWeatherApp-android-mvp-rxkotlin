package com.example.sfweather.databases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sfweather.models.SearchHistory
import io.reactivex.*

@Dao
interface SearchHistoryDAO {
    @Query("Select * From searchHistory ORDER BY timestamp DESC")
    fun getAll(): Single<List<SearchHistory>>

    @Query("Select * From searchHistory ORDER BY timestamp DESC LIMIT 1")
    fun getLatest(): Single<SearchHistory>

    @Query("Select count(cityId) From searchHistory WHERE cityId = :cityId")
    fun getCountByCityId(cityId: Int):Single<Int>

    @Insert
    fun insert(vararg searchHistory: SearchHistory):Completable

    @Update
    fun update(vararg searchHistory: SearchHistory):Completable

    @Query("DELETE FROM searchHistory WHERE cityId = :cityId")
    fun deleteByCityId(cityId: Int):Single<Int>
}
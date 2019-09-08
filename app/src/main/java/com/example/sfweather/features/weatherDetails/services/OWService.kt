package com.example.sfweather.features.weatherDetails.services

import com.example.sfweather.features.weatherDetails.models.OWResult
import com.example.sfweather.features.weatherDetails.repositories.OWRepository
import io.reactivex.Single
import org.koin.core.KoinComponent
import org.koin.core.inject

class OWService: KoinComponent {
    private val owRepository: OWRepository by inject()

    fun findByCityName(cityName: String): Single<OWResult> {
        return this.owRepository.findByCityName(cityName)
    }

    fun findByCityId(cityId: Int): Single<OWResult> {
        return this.owRepository.findByCityId(cityId)
    }

}
package com.example.sfweather.base

interface BasePresenter<V> {
    fun attachView(view: V)
    fun detachView()
}
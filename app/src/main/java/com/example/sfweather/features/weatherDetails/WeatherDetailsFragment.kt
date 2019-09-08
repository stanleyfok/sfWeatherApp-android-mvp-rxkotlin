package com.example.sfweather.features.weatherDetails

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sfweather.MainActivity
import com.example.sfweather.R
import com.example.sfweather.features.weatherHistory.WeatherHistoryFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_weather_detail.*
import android.content.Intent
import android.widget.ProgressBar
import com.example.sfweather.constants.AppConstants
import com.example.sfweather.utils.WeatherUtils
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.SearchViewQueryTextEvent
import com.jakewharton.rxbinding3.widget.queryTextChangeEvents
import io.reactivex.disposables.CompositeDisposable

class WeatherDetailsFragment : Fragment(), WeatherDetailsContract.View {
    private var presenter: WeatherDetailsContract.Presenter = WeatherDetailsPresenter()

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var isRecentSearchLoaded:Boolean = false
    private var cityIdToFetch:Int = -1

    //region life cycle
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View = inflater.inflate(R.layout.fragment_weather_detail, container, false)

        this.presenter.attachView(this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compositeDisposable.add(this.viewHistoryButton.clicks().subscribe{
            this.onViewHistoryButtonClick()
        })

        compositeDisposable.add(this.searchView.queryTextChangeEvents().subscribe{
            this.onQueryTextSubmit(it)
        })

        if (!isRecentSearchLoaded) {
            this.presenter.fetchLastStoredWeather()

            this.isRecentSearchLoaded = true
        } else {
            if (this.cityIdToFetch != -1) {
                this.presenter.fetchWeatherByCityId(cityIdToFetch)

                this.cityIdToFetch = -1;
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        this.compositeDisposable.clear()
        this.presenter.detachView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.REQ_CODE_FRAGMENT_SEARCH_HISTORY) {

                // bad code, however onActivityResult starts before onViewCreated
                // store to use in onViewCreated
                this.cityIdToFetch = data!!.getIntExtra("cityId", -1)
            }
        }
    }
    //endregion

    //region click listener
    private fun onViewHistoryButtonClick() {
        val fragment = WeatherHistoryFragment()
        fragment.setTargetFragment(this, AppConstants.REQ_CODE_FRAGMENT_SEARCH_HISTORY)

        (activity as MainActivity).replaceFragments(fragment)
    }
    //endregion

    //region searchView listener
    private fun onQueryTextSubmit(event: SearchViewQueryTextEvent) {
        if (event.isSubmitted) {
            this.presenter.fetchWeatherByCityName(event.queryText.toString())
            this.searchView.setQuery("", false)

            this.searchView.clearFocus()
        }
    }
    //endregion

    //region interface methods
    override fun setIsLoading(bool: Boolean) {
        this.progressBar.visibility = if (bool) ProgressBar.VISIBLE else ProgressBar.INVISIBLE
    }

    override fun updateView(data: WeatherDetailsData) {
        val displayTemp = String.format("%.1f", WeatherUtils.kelvinToCelsius(data.temperature)) + "°"

        this.cityNameLabel.text = data.cityName
        this.temperatureLabel.text = displayTemp
        this.weatherLabel.text = data.weatherDesc
    }

    override fun showErrorMessage(errorMessage: String) {
        Snackbar.make(this.view!!, errorMessage, Snackbar.LENGTH_SHORT).show();
    }
    //endregion
}

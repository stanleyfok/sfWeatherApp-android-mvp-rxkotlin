package com.example.sfweather.features.weatherDetails

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import com.example.sfweather.MainActivity
import com.example.sfweather.R
import com.example.sfweather.features.weatherHistory.WeatherHistoryFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_weather_detail.*
import android.content.Intent
import android.widget.ProgressBar
import com.example.sfweather.constants.AppConstants
import com.example.sfweather.utils.WeatherUtils

class WeatherDetailsFragment : Fragment(), WeatherDetailsContract.View, View.OnClickListener, SearchView.OnQueryTextListener {
    private var presenter: WeatherDetailsContract.Presenter = WeatherDetailsPresenter()

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

        this.viewHistoryButton.setOnClickListener(this)
        this.searchView.setOnQueryTextListener(this)

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
    override fun onClick(view: View) {
        when (view.id) {
            R.id.viewHistoryButton -> {
                val fragment = WeatherHistoryFragment()
                fragment.setTargetFragment(this, AppConstants.REQ_CODE_FRAGMENT_SEARCH_HISTORY)

                (activity as MainActivity).replaceFragments(fragment)
            }
        }
    }
    //endregion

    //region searchView listener
    override fun onQueryTextSubmit(p0: String?): Boolean {
        if (p0 != null && p0.isNotEmpty()) {
            this.presenter.fetchWeatherByCityName(p0)
            this.searchView.setQuery("", false)
        }

        this.searchView.clearFocus()

        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return false
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

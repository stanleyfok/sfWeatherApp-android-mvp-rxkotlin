package com.example.sfweather.features.weatherHistory.adapters

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sfweather.R
import com.example.sfweather.features.weatherHistory.WeatherHistoryContract
import com.jakewharton.rxbinding3.view.clicks
import kotlinx.android.synthetic.main.weather_history_item.view.*

class WeatherHistoryRecycleViewAdapter(
    private val presenter: WeatherHistoryContract.Presenter
) : RecyclerView.Adapter<WeatherHistoryRecycleViewAdapter.WeatherHistoryViewHolder>() {

    //region recycler view adapter override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHistoryViewHolder {
        return WeatherHistoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.weather_history_item, parent, false)
        )
    }

    override fun getItemCount() = this.presenter.getSearchHistoryCount()

    override fun onBindViewHolder(holder: WeatherHistoryViewHolder, position: Int) {
        val searchHistory = this.presenter.getSearchHistoryAtPosition(position)

        if (searchHistory != null) {
            holder.cityNameLabel.text = searchHistory.cityName
            holder.dateLabel.text = DateFormat.format("yyyy-MM-dd hh:mm:ss", searchHistory.timestamp * 1000L).toString()
            holder.deleteBtn.visibility = if (this.presenter.isEdit) View.VISIBLE else View.GONE

            with(holder.itemView) {
                tag = position
                clicks().subscribe{
                    onHolderClick(this)
                }
            }

            with(holder.deleteBtn) {
                tag = position
                clicks().subscribe{
                    onHolderDeleteClick(this)
                }
            }
        }
    }
    //endregion

    //region click handlers
    private fun onHolderClick(holder: View) {
        val position = holder.tag as Int

        this.presenter.selectSearchHistoryAtPosition(position)
    }

    private fun onHolderDeleteClick(holder: View) {
        val position = holder.tag as Int

        this.presenter.removeSearchHistoryAtPosition(position)
    }
    //endregion

    inner class WeatherHistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cityNameLabel: TextView = itemView.cityNameLabel
        val dateLabel: TextView = itemView.dateLabel
        var deleteBtn: ImageView = itemView.deleteBtn
    }
}

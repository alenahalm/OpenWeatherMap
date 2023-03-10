package com.example.openweathermap

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeatherAdapter (private var weatherList: ArrayList<Weather>) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var city = itemView.findViewById<TextView>(R.id.cityName)
        var temp = itemView.findViewById<TextView>(R.id.temp)
        var humidity = itemView.findViewById<TextView>(R.id.humidity)
        var icon = itemView.findViewById<ImageView>(R.id.icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false))
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.city.text = weatherList[position].city
        holder.temp.text = weatherList[position].temp
        holder.humidity.text = weatherList[position].humidity
        holder.icon.setImageResource(weatherList[position].iconID)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    fun updateAdapter() {
        notifyDataSetChanged()
    }
}
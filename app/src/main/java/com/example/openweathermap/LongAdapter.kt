package com.example.openweathermap

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LongAdapter(private var weatherList: ArrayList<Weather>) : RecyclerView.Adapter<LongAdapter.LongViewHolder>()  {

    class LongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var city = itemView.findViewById<TextView>(R.id.cityName)
        var temp = itemView.findViewById<TextView>(R.id.temp)
        var humidity = itemView.findViewById<TextView>(R.id.humidity)
        var wind = itemView.findViewById<TextView>(R.id.wind)
        var icon = itemView.findViewById<ImageView>(R.id.icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LongViewHolder {
        return LongViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.long_item, parent, false))
    }

    override fun onBindViewHolder(holder: LongViewHolder, position: Int) {
        holder.city.text = weatherList[position].city
        holder.temp.text = weatherList[position].temp
        holder.humidity.text = weatherList[position].humidity
        holder.wind.text = weatherList[position].wind
        holder.icon.setImageResource(weatherList[position].iconID)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    fun update(data: ArrayList<Weather>) {
        Log.d("123", data.size.toString())
        weatherList.clear()
        Log.d("123", weatherList.size.toString())
        weatherList = data
        notifyDataSetChanged()
        Log.d("123", weatherList.size.toString())
    }
}
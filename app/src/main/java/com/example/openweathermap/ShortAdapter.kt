package com.example.openweathermap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShortAdapter (private var weatherList: ArrayList<Weather>) : RecyclerView.Adapter<ShortAdapter.ShortViewHolder>() {

    class ShortViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var city = itemView.findViewById<TextView>(R.id.cityName)
        var temp = itemView.findViewById<TextView>(R.id.temp)
        var icon = itemView.findViewById<ImageView>(R.id.icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortViewHolder {
        return ShortViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false))
    }

    override fun onBindViewHolder(holder: ShortViewHolder, position: Int) {
        holder.city.text = weatherList[position].city
        holder.temp.text = weatherList[position].temp
        holder.icon.setImageResource(weatherList[position].iconID)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    fun update(data: ArrayList<Weather>) {
        weatherList.clear()
        weatherList = data
        notifyDataSetChanged()
    }
}
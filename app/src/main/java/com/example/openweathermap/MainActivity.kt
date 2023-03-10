package com.example.openweathermap

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.openweathermap.databinding.ActivityMainBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CountDownLatch
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: WeatherAdapter
    lateinit var API: String

    var weatherInfo = mutableListOf<Weather>()

    lateinit var _response: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("tag", "created")

        _response = ""
        API = resources.getString(R.string.API_key)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = WeatherAdapter(weatherInfo as ArrayList<Weather>)

        binding.citiesList.layoutManager = LinearLayoutManager(this)
        binding.citiesList.adapter = adapter

        binding.citiesList.apply {
            layoutManager = if (isVertical()) {
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            } else {
                LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            }
        }

        binding.submit.setOnClickListener {
            val cityName = binding.citySearch.text.toString()
            if (cityName != "") {
                val weather = openWeatherMap(cityName)
                weatherInfo.add(weather)
                binding.citySearch.setText("")
                adapter.updateAdapter()
            }
        }

        binding.update.setOnClickListener {
            this.updateWeather()
        }


    }

    private fun isVertical(): Boolean {
        return when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> true
            else -> false
        }
    }

    private fun updateWeather() {
        Log.d("tag", "${weatherInfo.size}")
        for (i in 0 until weatherInfo.size) {
            val weather = openWeatherMap(weatherInfo[i].city)
            weatherInfo[i] = weather
        }
        adapter.updateAdapter()
    }

    private fun openWeatherMap(city_name: String) : Weather{

        val url = "https://api.openweathermap.org/data/2.5/weather?q=${city_name}&appid=${API}"
        val weather = Weather()
        weather.city = city_name
        val response = request(url)

        try {
            val jsonObject = JSONObject(response)

            weather.temp = jsonObject.getJSONObject("main").getString("temp").let { "${(it.toFloat()-273.15).roundToInt()}°С" }
            weather.humidity = jsonObject.getJSONObject("main").getString("humidity").let { "Humidity: $it%" }

            val icon = JSONObject(jsonObject.getJSONArray("weather").getString(0)).getString("icon").let { "_${it}" }
            weather.iconID = resources.getIdentifier(icon, "drawable", packageName)
        } catch (_: IOException) {}

        Log.d("tag", "openweathermap $weather")
        return weather
    }

    private fun request(url: String) : String  {

        var countDownLatch = CountDownLatch(1)

        val httpClient = OkHttpClient()
        val request = Request.Builder().url(url).build()

        httpClient.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.d("tag", "Failure")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.d("tag", "HTTP Error")
                    } else {
                        val body = response?.body?.string().toString()
                        _response = body
                        countDownLatch.countDown()
                    }
                }
            }
        })
        countDownLatch.await()
        return _response
    }
}
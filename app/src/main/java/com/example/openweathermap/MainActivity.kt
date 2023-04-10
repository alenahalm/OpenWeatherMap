package com.example.openweathermap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.openweathermap.databinding.ActivityMainBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.CountDownLatch
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var currentFragment: Fragment? = null
    private lateinit var fm: FragmentManager

    lateinit var API: String

    lateinit var _response: String

    var weatherInfo = mutableListOf<Weather>()

    var isLong = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (isLong) {
            currentFragment = LongFragment(ArrayList(weatherInfo), this)
        } else {
            currentFragment = ShortFragment(ArrayList(weatherInfo), this)
        }
        fm = supportFragmentManager
        fm.beginTransaction()
            .add(R.id.container, currentFragment!!)
            .commit()

        Log.d("tag", "created")

        _response = ""
        API = resources.getString(R.string.API_key)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.submit.setOnClickListener {
            val cityName = binding.citySearch.text.toString()
            if (cityName != "") {
                val weather = openWeatherMap(cityName)
                weatherInfo.add(weather)
                binding.citySearch.setText("")
                if (isLong) {
                    (currentFragment as LongFragment)!!.update(ArrayList(weatherInfo))
                } else {
                    (currentFragment as ShortFragment)!!.update(ArrayList(weatherInfo))
                }
            }
        }

        binding.update.setOnClickListener {
            this.updateWeather()
        }

        binding.change.setOnClickListener {
            isLong = !isLong
            if (isLong) {
                currentFragment = LongFragment(ArrayList(weatherInfo), this)
            } else {
                currentFragment = ShortFragment(ArrayList(weatherInfo), this)
            }
            fm.beginTransaction()
                .replace(R.id.container, currentFragment!!)
                .commit()
        }

    }

    private fun updateWeather() {
        Log.d("tag", "${weatherInfo.size}")
        for (i in 0 until weatherInfo.size) {
            val weather = openWeatherMap(weatherInfo[i].city)
            weatherInfo[i] = weather
        }
        if (isLong) {
            (currentFragment as LongFragment)!!.update(ArrayList(weatherInfo))
        } else {
            (currentFragment as ShortFragment)!!.update(ArrayList(weatherInfo))
        }
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
            weather.wind = jsonObject.getJSONObject("wind").getString("speed").let { "Wind speed: $it m/s" }

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
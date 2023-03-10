package com.example.openweathermap

class Weather {
    var city = ""
    var temp = ""
    var humidity = ""
    var iconID = 0

    override fun toString(): String {
        return "city: $city, temp: $temp, $humidity"
    }
}
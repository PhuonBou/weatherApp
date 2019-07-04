package fr.phuon.android.weatherapp.model.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {

    @GET("/data/2.5/weather")
    fun getCityWeather(@Query("APPID") apiKey: String, @Query("q") cityName: String): Call<WeatherResponse>
}
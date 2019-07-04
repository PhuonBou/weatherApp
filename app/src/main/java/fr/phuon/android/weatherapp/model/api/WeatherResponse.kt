package fr.phuon.android.weatherapp.model.api

class WeatherResponse {

    var coord: Coord? = null

    var weather: Array<Weather>? = null

    var base: String? = null

    var main: Main? = null

    var visibility: String? = null

    var wind: Wind? = null

    var clouds: Clouds? = null

    var dt: String? = null

    var sys: Sys? = null

    var timezone: String? = null

    var id: String? = null

    var name: String? = null

    var cod: String? = null
}
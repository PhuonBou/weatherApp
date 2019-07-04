package fr.phuon.android.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.*
import fr.phuon.android.weatherapp.model.api.OpenWeatherService
import fr.phuon.android.weatherapp.model.api.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val LOG_TAG = this.javaClass.simpleName

    private val baseUrl = "https://api.openweathermap.org"

    lateinit var cityEditText: EditText
    lateinit var okButton: Button

//    lateinit var weatherImageView: ImageView
    lateinit var descTextView: TextView
    lateinit var tempTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val retrofit = Retrofit.Builder()
            .baseUrl(this.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(OpenWeatherService::class.java)


        this.cityEditText = this.findViewById(R.id.cityEditText)
        this.okButton = this.findViewById(R.id.okButton)

//        this.weatherImageView = this.findViewById(R.id.weatherImageView)
        this.descTextView = this.findViewById(R.id.descTextView)
        this.tempTextView = this.findViewById(R.id.tempTextView)

        this.okButton.setOnClickListener {

            val cityName = this.cityEditText.text.toString()
            if (cityName.isEmpty()) {
                Toast.makeText(this, getString(R.string.city_et_empty), Toast.LENGTH_SHORT).show()
            } else {
                // Envoyer la requete
                Log.d(LOG_TAG, cityName)
                val getCityWeather = service.getCityWeather(BuildConfig.API_KEY, cityName)
                getCityWeather.enqueue(object : Callback<WeatherResponse> {

                    override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                        // Peut mieux faire comme check le code 404 plutot
                        if (t.message.equals("city not found"))
                            Log.d(LOG_TAG, "Failure: " + t.message)
                    }

                    override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                        when (response.code()) {
                            200 -> {
                                Log.d(LOG_TAG, "Success: " + response.body()!!.coord!!.lat)
                                //I wanted to display an image depending on the weather but i ran out of time.
                                val description = response.body()!!.weather?.get(0)!!.description
                                descTextView.text = description

                                //Default temp is in Kelvin
                                val temp = (response.body()!!.main!!.temp!!.toDouble() - 273.15).roundToInt().toString()+"°C"
                                tempTextView.text = temp
                            }
                            404 -> {
                                Toast.makeText(
                                    applicationContext,
                                    getString(R.string.city_does_not_exists),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else -> {
                                Toast.makeText(
                                    applicationContext,
                                    getString(R.string.other_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                })
            }

            this.cityEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }
    }


}

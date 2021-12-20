package au.com.lexicon.herecomesthesun.service

import au.com.lexicon.herecomesthesun.domain.service.Weather
import au.com.lexicon.herecomesthesun.service.api.WeatherApi
import retrofit2.Retrofit

class WeatherService(
    retrofit: Retrofit
): Weather {
    private val client = retrofit.create(WeatherApi::class.java)
}

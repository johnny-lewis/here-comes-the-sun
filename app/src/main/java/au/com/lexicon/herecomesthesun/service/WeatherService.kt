package au.com.lexicon.herecomesthesun.service

import au.com.lexicon.herecomesthesun.domain.model.GeoLocationData
import au.com.lexicon.herecomesthesun.domain.model.WeatherData
import au.com.lexicon.herecomesthesun.domain.service.Weather
import au.com.lexicon.herecomesthesun.service.api.WeatherApi
import au.com.lexicon.herecomesthesun.service.mapper.mapToDomain
import au.com.lexicon.herecomesthesun.service.model.ServiceResult
import retrofit2.Retrofit
import timber.log.Timber

class WeatherService(
    retrofit: Retrofit
): Weather {
    private val client = retrofit.create(WeatherApi::class.java)

    override suspend fun get7DayForecast(geoLocationData: GeoLocationData): ServiceResult<WeatherData, String> =
        try {
            client.getForecast(
                latlng = geoLocationData.toString(),
                days = 7
            ).let { response ->
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    ServiceResult.Success(body.mapToDomain())
                } else {
                    ServiceResult.Error("Failed")
                }
            }
        } catch (e: Exception) {
            Timber.e("Failed to get 7 day forecast", e)
            ServiceResult.Error("Failed")
        }
}

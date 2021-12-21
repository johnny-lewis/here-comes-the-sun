package au.com.lexicon.herecomesthesun.domain.usecase

import au.com.lexicon.herecomesthesun.domain.model.GeoLocationData
import au.com.lexicon.herecomesthesun.domain.model.WeatherData
import au.com.lexicon.herecomesthesun.domain.service.Weather
import au.com.lexicon.herecomesthesun.service.model.ServiceResult
import javax.inject.Inject

class GetWeatherDataUseCase @Inject constructor(
    private val weather: Weather
) {
    suspend operator fun invoke(geoLocationData: GeoLocationData): WeatherData? =
        when (val result = weather.get7DayForecast(geoLocationData)) {
            is ServiceResult.Success -> result.data
            else -> null
        }
}

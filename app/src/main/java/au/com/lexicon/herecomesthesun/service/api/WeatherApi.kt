package au.com.lexicon.herecomesthesun.service.api

import au.com.lexicon.herecomesthesun.service.model.WeatherForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast.json")
    suspend fun getForecast(
        @Query("q") latlng: String,
        @Query("days") days: Int
    ): Response<WeatherForecastResponse>
}

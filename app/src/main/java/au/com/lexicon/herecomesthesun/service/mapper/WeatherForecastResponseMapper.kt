package au.com.lexicon.herecomesthesun.service.mapper

import au.com.lexicon.herecomesthesun.domain.model.*
import au.com.lexicon.herecomesthesun.service.model.*
import java.time.Instant

fun WeatherForecastResponse.mapToDomain(): WeatherData =
    WeatherData(
        location = location.mapToDomain(),
        currentWeather = current.mapToDomain(),
        forecast = forecast.forecastday.map {
            it.mapToDomain()
        }
    )

private fun WeatherForecastLocation.mapToDomain(): LocationData =
    LocationData(
        suburb = name,
        region = region,
        country = country
    )

private fun WeatherForecastCurrent.mapToDomain(): CurrentWeather =
    CurrentWeather(
        temperature = temp_c,
        cloud = cloud,
        uv = uv,
        condition = WeatherCondition.codeToCondition(condition.code)
    )

private fun WeatherForecastDay.mapToDomain(): ForecastDay =
    ForecastDay(
        date = Instant.ofEpochSecond(date_epoch),
        maxTemp = day.maxtemp_c,
        minTemp = day.mintemp_c,
        avgTemp = day.avgtemp_c,
        uv = day.uv,
        hours = hour.map {
            it.mapToDomain()
        }
    )

private fun WeatherForecastHour.mapToDomain(): ForecastHour =
    ForecastHour(
        time = Instant.ofEpochSecond(time_epoch),
        temperature = temp_c,
        cloud = cloud,
        uv = uv
    )

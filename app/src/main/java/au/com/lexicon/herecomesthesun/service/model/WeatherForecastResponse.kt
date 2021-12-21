package au.com.lexicon.herecomesthesun.service.model

data class WeatherForecastResponse(
    val current: WeatherForecastCurrent,
    val forecast: Forecast,
    val location: WeatherForecastLocation
)

data class WeatherForecastCurrent(
    val cloud: Int,
    val condition: WeatherForecastCondition,
    val humidity: Int,
    val is_day: Int,
    val temp_c: Double,
    val uv: Double
)

data class Forecast(
    val forecastday: List<WeatherForecastDay>
)

data class WeatherForecastLocation(
    val country: String,
    val lat: Double,
    val localtime: String,
    val localtime_epoch: Int,
    val lon: Double,
    val name: String,
    val region: String,
    val tz_id: String
)

data class WeatherForecastCondition(
    val text: String,
    val code: Int
)

data class WeatherForecastDay(
    val date_epoch: Long,
    val day: WeatherForecastDayData,
    val hour: List<WeatherForecastHour>
)


data class WeatherForecastDayData(
    val avgtemp_c: Double,
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val uv: Double
)

data class WeatherForecastHour(
    val cloud: Int,
    val temp_c: Double,
    val time: String,
    val time_epoch: Long,
    val uv: Double
)

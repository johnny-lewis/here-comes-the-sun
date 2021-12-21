package au.com.lexicon.herecomesthesun.domain.model

import java.time.Instant

data class WeatherData(
    val location: LocationData,
    val currentWeather: CurrentWeather,
    val forecast: List<ForecastDay>
)

data class LocationData(
    val suburb: String,
    val region: String,
    val country: String
)

data class CurrentWeather(
    val temperature: Double,
    val cloud: Int,
    val uv: Double,
    val condition: WeatherCondition
)

enum class WeatherCondition {
    Unknown,
    Clear,
    Cloudy,
    Rain,
    Snow;

    companion object {
        fun codeToCondition(code: Int): WeatherCondition =
            when (code) {
                1000 -> Clear
                1003, 1006, 1009,
                1030, 1135, 1147 -> Cloudy
                1063, 1072, 1087, 1150, 1153,
                1168, 1171, 1180, 1183, 1186,
                1189, 1192, 1195, 1198, 1201,
                1240, 1243, 1246, 1273, 1276, -> Rain
                1066, 1069, 1114, 1117, 1204,
                1207, 1210, 1213, 1216, 1219,
                1222, 1225, 1237, 1249, 1252,
                1255, 1258, 1261, 1264, 1279, 1282 -> Snow
                else -> Unknown
            }
    }
}

data class ForecastDay(
    val maxTemp: Double,
    val hours: List<ForecastHour>
)

data class ForecastHour(
    val time: Instant
)

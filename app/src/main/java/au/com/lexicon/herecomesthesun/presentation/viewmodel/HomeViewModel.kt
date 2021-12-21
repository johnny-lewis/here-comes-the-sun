package au.com.lexicon.herecomesthesun.presentation.viewmodel

import android.os.SystemClock
import androidx.lifecycle.viewModelScope
import au.com.lexicon.herecomesthesun.domain.model.ForecastDay
import au.com.lexicon.herecomesthesun.domain.model.GraphPoint
import au.com.lexicon.herecomesthesun.domain.usecase.GetCurrentLocationUseCase
import au.com.lexicon.herecomesthesun.domain.usecase.GetWeatherDataUseCase
import au.com.lexicon.herecomesthesun.domain.usecase.ResolveLocationPermissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.hours

typealias HomeNextScreen = () -> Unit

interface HomeViewModelContract {
    val locationFlow: StateFlow<String>
    val topEfficienciesFlow: StateFlow<List<Pair<String, String>>>
    val UVFlow: StateFlow<UVRatingGrades>
    val dayFlow: StateFlow<Int>
    val dataDayFlow: StateFlow<List<Pair<ForecastDay, Int>>>
    val timeFlow: StateFlow<Int>
    val dataTimeFlow: StateFlow<List<Int>>
    val graphValuesFlow: StateFlow<List<GraphPoint>>
    val yAxisValuesFlow: StateFlow<List<Int>>
    fun setSettingsScreen(next: HomeNextScreen)
    fun goToSettingsScreen()
    suspend fun goNextTime()
    suspend fun goPreviousTime()
    suspend fun changeDay(day: Int)
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    resolveLocationPermission: ResolveLocationPermissionUseCase,
    private val getCurrentLocation: GetCurrentLocationUseCase,
    private val getWeatherData: GetWeatherDataUseCase
) : BaseViewModel<HomeNextScreen>(), HomeViewModelContract {
    companion object {
        private const val yAxisPadding = 3
    }

    private val currentDayEfficiencies = mutableListOf<Pair<String, Int>>()

    private val _topEfficienciesFlow = MutableStateFlow(emptyList<Pair<String, String>>())
    override val topEfficienciesFlow: StateFlow<List<Pair<String, String>>> = _topEfficienciesFlow

    private val _locationFlow = MutableStateFlow("-")
    override val locationFlow: StateFlow<String> = _locationFlow.asStateFlow()

    private val _dayFlow = MutableStateFlow(0)
    override val dayFlow = _dayFlow.asStateFlow()

    private val _dataDayFlow = MutableStateFlow(emptyList<Pair<ForecastDay, Int>>())
    override val dataDayFlow = _dataDayFlow.asStateFlow()

    private val _timeFlow = MutableStateFlow(0)
    override val timeFlow = _timeFlow.asStateFlow()

    override val dataTimeFlow = combine(_dayFlow, _dataDayFlow) { day, data ->
        if (data.isNotEmpty()) {
            val dayData = data[day].first
            when (day) {
                0 -> {
                    List(size = 6) {
                        (calculateEfficiency(
                            uv = dayData.hours[it].uv,
                            cloud = dayData.hours[it].cloud,
                            temp = dayData.hours[it].temperature,
                            time = dayData.hours[it].time
                        )* 100 ).roundToInt()
                    }
                }
                1 -> {
                    List(size = 6) {
                        (calculateEfficiency(
                            uv = dayData.hours[it + 6].uv,
                            cloud = dayData.hours[it].cloud,
                            temp = dayData.hours[it].temperature,
                            time = dayData.hours[it].time
                        )* 100 ).roundToInt()
                    }
                }
                2 -> {
                    List(size = 6) {
                        (calculateEfficiency(
                            uv = dayData.hours[it + 12].uv,
                            cloud = dayData.hours[it].cloud,
                            temp = dayData.hours[it].temperature,
                            time = dayData.hours[it].time
                        )* 100 ).roundToInt()
                    }
                }
                else -> {
                    List(size = 6) {
                        (calculateEfficiency(
                            uv = dayData.hours[it + 18].uv,
                            cloud = dayData.hours[it].cloud,
                            temp = dayData.hours[it].temperature,
                            time = dayData.hours[it].time
                        )* 100 ).roundToInt()
                    }
                }
            }
        } else {
            emptyList()
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = listOf()
        )

    override val graphValuesFlow = dataTimeFlow.map { data ->
        if (data.isNotEmpty()) {
            List(size = 6) {
                val value = data[it]
                GraphPoint(
                    time = 0,
                    value = value,
                    grade = if (value < 10) {
                        UVRatingGrades.NIGHT
                    } else if (value < 40) {
                        UVRatingGrades.BAD
                    } else if (value < 70) {
                        UVRatingGrades.OK
                    } else {
                        UVRatingGrades.GOOD
                    }
                )
            }
        } else {
            emptyList()
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = listOf()
        )


    override val UVFlow = _topEfficienciesFlow.map { data ->
        if (data.isNotEmpty()) {
           if (data.first().second.replace("%", "").toInt() < 10) {
               UVRatingGrades.NIGHT
           } else if (data.first().second.replace("%", "").toInt() < 40) {
               UVRatingGrades.BAD
           } else if (data.first().second.replace("%", "").toInt() < 70) {
               UVRatingGrades.OK
           } else {
               UVRatingGrades.GOOD
           }
        } else {
            UVRatingGrades.UNKNOWN
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = UVRatingGrades.UNKNOWN
        )

    override val yAxisValuesFlow: StateFlow<List<Int>> =
        graphValuesFlow.map { sensorData ->
            if (sensorData.isNotEmpty()) {
                val max =
                    if (sensorData.maxByOrNull { it.value }?.value?.plus(yAxisPadding) ?: 0 >= 0) {
                        ceilToTen(
                            (sensorData.maxByOrNull { it.value }?.value?.plus(yAxisPadding) ?: 0)
                        )
                    } else {
                        ((sensorData.maxByOrNull { it.value }?.value?.plus(yAxisPadding)
                            ?: 0) / 10) * 10
                    }

                val min =
                    if (sensorData.minByOrNull { it.value }?.value?.minus(yAxisPadding) ?: 0 >= 0) {
                        floorToTen(
                            (sensorData.minByOrNull { it.value }?.value?.minus(yAxisPadding) ?: 0)
                        )
                    } else {
                        ((sensorData.minByOrNull { it.value }?.value?.minus(yAxisPadding)
                            ?: 0) / 10 - 1) * 10
                    }
                val values = List(size = 2) {
                    when (it) {
                        0 -> max
                        else -> min
                    }
                }
                values
            } else {
                emptyList()
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = listOf()
            )

    init {
        resolveLocationPermission { allowed ->
            if (allowed) {
                getWeather()
            }
        }
    }

    private fun getWeather() = viewModelScope.launch {
        getCurrentLocation()?.let { location ->
            getWeatherData(
                geoLocationData = location
            )?.let { weatherData ->
                _dataDayFlow.emit(
                    List(size = weatherData.forecast.size) {
                        Pair(
                            weatherData.forecast[it],
                            (calculateEfficiency(
                                uv = weatherData.forecast[it].uv,
                                cloud = weatherData.forecast[it].hours.first().cloud,
                                temp = weatherData.forecast[it].avgTemp,
                                time = weatherData.forecast[it].hours[12].time
                            )* 100 ).roundToInt()
                        )
                    }
                )
                _locationFlow.emit("${weatherData.location.suburb}, ${weatherData.location.region}")
                currentDayEfficiencies.clear()
                weatherData.forecast.first().hours.forEach { forecastHour ->
                    val hour = forecastHour.time.atZone(ZoneId.systemDefault()).hour
                    currentDayEfficiencies.add(hour.toString() to (calculateEfficiency(
                        uv = forecastHour.uv,
                        cloud = forecastHour.cloud,
                        temp = forecastHour.temperature,
                        time = forecastHour.time
                    ) * 100).roundToInt())
                }

                currentDayEfficiencies.sortedBy {
                    it.second
                }.let {
                    _topEfficienciesFlow.emit(listOf(
                        it[it.size - 1].first to "${it[it.size - 1].second}%",
                        it[it.size - 2].first to "${it[it.size - 2].second}%",
                        it[it.size - 3].first to "${it[it.size - 3].second}%"
                    ))
                }
            }
        }
    }

    override fun setSettingsScreen(next: HomeNextScreen) {
        nextScreen = next
    }

    override fun goToSettingsScreen() {
        nextScreen()
    }

    override suspend fun goNextTime() {
        if (_timeFlow.value != 3) {
            _timeFlow.emit(_timeFlow.value + 1)
        }
    }

    override suspend fun goPreviousTime() {
        if (_timeFlow.value != 0) {
            _timeFlow.emit(_timeFlow.value - 1)
        }
    }

    override suspend fun changeDay(day: Int) {
        _dayFlow.emit(day)
    }

    fun floorToTen(num: Int): Int {
        return (num / 10) * 10
    }

    fun ceilToTen(num: Int): Int {
        return (num / 10 + 1) * 10
    }

    // region Efficiency calculations

    private fun calculateCloudMultiplier(cloud: Int): Double =
        -0.01 * cloud + 1

    private fun calculateTemperatureMultiplier(temp: Double) =
        if (temp <= 10 || (temp > 35 && temp <= 40)) {
            0.8
        } else if ((temp > 10 && temp <= 20) || (temp < 30 && temp <= 35)) {
            0.9
        } else if (temp < 20 && temp <= 30) {
            1.0
        } else {
            0.6
        }

    private fun calculateTimeMultiplier(time: Instant): Double =
        time.atZone(ZoneId.systemDefault()).hour.let { hour24 ->
            if (hour24 < 9 || hour24 >= 17) {
                0.0
            } else if ((hour24 in 9..10) || hour24 in 15..16) {
                0.75
            } else {
                1.0
            }
        }

    private fun calculateUVMultiplier(uv: Double): Double =
        uv.roundToInt().let {
            if (it >= 12) {
                0.8
            } else if (it >= 9) {
                0.9
            } else if (it >= 6) {
                1.0
            } else if (it >= 4) {
                0.9
            } else {
                0.8
            }
        }

    private fun calculateEfficiency(uv: Double, cloud: Int, temp: Double, time: Instant) =
        (calculateUVMultiplier(uv) + calculateCloudMultiplier(cloud) + calculateTemperatureMultiplier(temp)) * 0.33 * calculateTimeMultiplier(time)

    // endregion
}

enum class UVRatingGrades {
    UNKNOWN,
    NIGHT,
    BAD,
    OK,
    GOOD
}

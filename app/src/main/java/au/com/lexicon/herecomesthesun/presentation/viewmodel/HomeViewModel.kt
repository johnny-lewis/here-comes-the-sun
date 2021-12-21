package au.com.lexicon.herecomesthesun.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import au.com.lexicon.herecomesthesun.domain.model.GraphPoint
import au.com.lexicon.herecomesthesun.domain.usecase.GetCurrentLocationUseCase
import au.com.lexicon.herecomesthesun.domain.usecase.GetWeatherDataUseCase
import au.com.lexicon.herecomesthesun.domain.usecase.ResolveLocationPermissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject
import kotlin.math.roundToInt

typealias HomeNextScreen = () -> Unit

interface HomeViewModelContract {
    val messageFlow: SharedFlow<String>
    val UVFlow: StateFlow<UVRatingGrades>
    val dayFlow: StateFlow<Int>
    val timeFlow: StateFlow<Int>
    val graphValuesFlow: StateFlow<List<GraphPoint>>
    val yAxisValuesFlow: StateFlow<List<Int>>
    fun setSettingsScreen(next: HomeNextScreen)
    fun goToSettingsScreen()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentLocation: GetCurrentLocationUseCase,
    private val resolveLocationPermission: ResolveLocationPermissionUseCase,
    private val getWeatherData: GetWeatherDataUseCase
) : BaseViewModel<HomeNextScreen>(), HomeViewModelContract {

    companion object {
        private val UV_MULTIPLIER = listOf(0.8, 0.8, 0.8, 0.9, 0.9, 1, 1, 1, 0.9, 0.9, 0.9, 0.8, 0.8)

        private const val yAxisPadding = 3
    }

    private val _messageFlow = MutableSharedFlow<String>(replay = 1)
    override val messageFlow: SharedFlow<String> = _messageFlow.asSharedFlow()

    private val _UVFlow = MutableStateFlow(UVRatingGrades.UNKNOWN)
    override val UVFlow = _UVFlow.asStateFlow()

    private val _dayFlow = MutableStateFlow(0)
    override val dayFlow = _dayFlow.asStateFlow()

    private val _timeFlow = MutableStateFlow(0)
    override val timeFlow = _timeFlow.asStateFlow()

    private val _graphValuesFlow = MutableStateFlow(emptyList<GraphPoint>())
    override val graphValuesFlow = _graphValuesFlow.asStateFlow()

    override val yAxisValuesFlow: StateFlow<List<Int>> =
        _graphValuesFlow.map { sensorData ->
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
        viewModelScope.launch {
            _graphValuesFlow.emit(
                listOf(
                    GraphPoint(
                        time = 0,
                        value = 10,
                        grade = UVRatingGrades.NIGHT
                    ),
                    GraphPoint(
                        time = 1,
                        value = 90,
                        grade = UVRatingGrades.GOOD
                    ),
                    GraphPoint(
                        time = 2,
                        value = 30,
                        grade = UVRatingGrades.OK
                    ),
                    GraphPoint(
                        time = 3,
                        value = 10,
                        grade = UVRatingGrades.NIGHT
                    ),
                    GraphPoint(
                        time = 4,
                        value = 50,
                        grade = UVRatingGrades.OK
                    ),
                    GraphPoint(
                        time = 5,
                        value = 18,
                        grade = UVRatingGrades.NIGHT
                    )
                )
            )
            delay(2000)
            _UVFlow.emit(UVRatingGrades.NIGHT)
            delay(2000)
            _UVFlow.emit(UVRatingGrades.BAD)
            delay(2000)
            _UVFlow.emit(UVRatingGrades.OK)
            delay(2000)
            _UVFlow.emit(UVRatingGrades.GOOD)
        }
    }

    private fun showCurrentLocation() = viewModelScope.launch {
        _messageFlow.emit(getCurrentLocation()?.let {
            "Latitude: ${it.latitude}\nLongitude: ${it.longitude}"
        } ?: "Failed to get current location")
    }

    private fun getWeather() = viewModelScope.launch {
        getCurrentLocation()?.let { location ->
            getWeatherData(
                geoLocationData = location
            )?.let { weatherData ->
                println("++++ today max: ${weatherData.forecast.first().maxTemp}")
            }
        }
    }

    override fun setSettingsScreen(next: HomeNextScreen) {
        nextScreen = next
    }

    override fun goToSettingsScreen() {
        nextScreen()
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
        (calculateUVMultiplier(uv) + calculateCloudMultiplier(cloud) + calculateTemperatureMultiplier(temp)) * (1/3) * calculateTimeMultiplier(time)

    // endregion
}

enum class UVRatingGrades {
    UNKNOWN,
    NIGHT,
    BAD,
    OK,
    GOOD
}

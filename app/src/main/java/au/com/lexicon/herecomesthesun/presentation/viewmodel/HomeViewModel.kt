package au.com.lexicon.herecomesthesun.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import au.com.lexicon.herecomesthesun.domain.model.GraphPoint
import au.com.lexicon.herecomesthesun.domain.usecase.GetCurrentLocationUseCase
import au.com.lexicon.herecomesthesun.domain.usecase.GetWeatherDataUseCase
import au.com.lexicon.herecomesthesun.domain.usecase.ResolveLocationPermissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

typealias HomeNextScreen = () -> Unit

interface HomeViewModelContract {
    val UVFlow: StateFlow<UVRatingGrades>
    val dayFlow: StateFlow<Int>
    val timeFlow: StateFlow<Int>
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
    private val getCurrentLocation: GetCurrentLocationUseCase,
    private val resolveLocationPermission: ResolveLocationPermissionUseCase,
    private val getWeatherData: GetWeatherDataUseCase
) : BaseViewModel<HomeNextScreen>(), HomeViewModelContract {

    companion object {
        private const val yAxisPadding = 3
    }

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
                        grade = UVRatingGrades.BAD
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
                        grade = UVRatingGrades.BAD
                    ),
                    GraphPoint(
                        time = 4,
                        value = 50,
                        grade = UVRatingGrades.OK
                    ),
                    GraphPoint(
                        time = 5,
                        value = 18,
                        grade = UVRatingGrades.BAD
                    )
                )
            )
        }
    }

    private fun getWeather() = viewModelScope.launch {
        getCurrentLocation()?.let { location ->
            val result = getWeatherData(
                geoLocationData = location
            )

            Timber.i("++++ Weather data", result)
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
}

enum class UVRatingGrades {
    UNKNOWN,
    NIGHT,
    BAD,
    OK,
    GOOD
}

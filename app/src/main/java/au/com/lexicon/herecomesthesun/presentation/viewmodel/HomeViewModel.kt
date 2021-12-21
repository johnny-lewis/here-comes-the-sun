package au.com.lexicon.herecomesthesun.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import au.com.lexicon.herecomesthesun.domain.model.GraphPoint
import au.com.lexicon.herecomesthesun.domain.usecase.GetCurrentLocationUseCase
import au.com.lexicon.herecomesthesun.domain.usecase.ResolveLocationPermissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias HomeNextScreen = () -> Unit

interface HomeViewModelContract {
    val messageFlow: SharedFlow<String>
    val UVFlow: StateFlow<UVRatingGrades>
    val dayFlow: StateFlow<Int>
    val graphValuesFlow: StateFlow<List<GraphPoint>>
    val yAxisValuesFlow: StateFlow<List<Int>>
    fun setSettingsScreen(next: HomeNextScreen)
    fun goToSettingsScreen()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentLocation: GetCurrentLocationUseCase,
    private val resolveLocationPermission: ResolveLocationPermissionUseCase
) : BaseViewModel<HomeNextScreen>(), HomeViewModelContract {

    companion object {
        private const val yAxisPadding = 3
    }

    private val _messageFlow = MutableSharedFlow<String>(replay = 1)
    override val messageFlow: SharedFlow<String> = _messageFlow.asSharedFlow()

    private val _UVFlow = MutableStateFlow(UVRatingGrades.UNKNOWN)
    override val UVFlow = _UVFlow.asStateFlow()

    private val _dayFlow = MutableStateFlow(0)
    override val dayFlow = _dayFlow.asStateFlow()

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
                val values = List(size = 3) {
                    when (it) {
                        0 -> max
                        1 -> min + ((max - min) / 2)
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
                showCurrentLocation()
            }
        }
        viewModelScope.launch {
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
}

enum class UVRatingGrades {
    UNKNOWN,
    NIGHT,
    BAD,
    OK,
    GOOD
}
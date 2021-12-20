package au.com.lexicon.herecomesthesun.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import au.com.lexicon.herecomesthesun.domain.usecase.GetCurrentLocationUseCase
import au.com.lexicon.herecomesthesun.domain.usecase.ResolveLocationPermissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias HomeNextScreen = () -> Unit

interface HomeViewModelContract {
    val messageFlow: SharedFlow<String>
    fun setSettingsScreen(next: HomeNextScreen)
    fun goToSettingsScreen()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentLocation: GetCurrentLocationUseCase,
    private val resolveLocationPermission: ResolveLocationPermissionUseCase
) : BaseViewModel<HomeNextScreen>(), HomeViewModelContract {
    private val _messageFlow = MutableSharedFlow<String>(replay = 1)
    override val messageFlow: SharedFlow<String> = _messageFlow.asSharedFlow()

    init {
        resolveLocationPermission { allowed ->
            if (allowed) {
                showCurrentLocation()
            }
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
}

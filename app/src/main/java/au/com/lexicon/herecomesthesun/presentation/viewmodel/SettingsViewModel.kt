package au.com.lexicon.herecomesthesun.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

interface SettingsViewModelContract {
    val messageFlow: SharedFlow<String>
}

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel(), SettingsViewModelContract {
    private val _messageFlow = MutableSharedFlow<String>(replay = 1)
    override val messageFlow: SharedFlow<String> = _messageFlow.asSharedFlow()

    init {

    }
}

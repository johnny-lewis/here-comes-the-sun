package au.com.lexicon.herecomesthesun.presentation.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

typealias LaunchNextScreen = () -> Unit

interface LaunchViewModelContract {
    fun setHomeScreen(next: HomeNextScreen)
    fun goToHomeScreen()
}

@HiltViewModel
class LaunchViewModel @Inject constructor() : BaseViewModel<LaunchNextScreen>(), LaunchViewModelContract {
    override fun setHomeScreen(next: LaunchNextScreen) {
        nextScreen = next
    }

    override fun goToHomeScreen() {
        nextScreen()
    }
}
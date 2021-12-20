package au.com.lexicon.herecomesthesun.presentation.viewmodel

import androidx.lifecycle.ViewModel

open class BaseViewModel<TNextScreen : Any>: ViewModel() {
    protected lateinit var nextScreen: TNextScreen
}

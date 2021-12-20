package au.com.lexicon.herecomesthesun.domain.service

import android.content.Context

interface Permission {
    fun provideActivityContext(activityContext: Context)
    fun hasLocationPermission(): Boolean
    fun requestLocationPermission(callback: ((Boolean) -> Unit)? = null)
    fun onRequestLocationFinished(allowed: Boolean)
}

package au.com.lexicon.herecomesthesun.service

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import au.com.lexicon.herecomesthesun.domain.service.Permission

class PermissionService : Permission {
    companion object {
        const val LOCATION_REQUEST_CODE: Int = 69
    }

    private lateinit var context: Context

    private var locationPermissionCallback: ((Boolean) -> Unit)? = null

    override fun provideActivityContext(activityContext: Context) {
        context = activityContext
    }

    override fun hasLocationPermission(): Boolean =
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    override fun requestLocationPermission(callback: ((Boolean) -> Unit)?) {
        locationPermissionCallback = callback
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE
        )
    }

    override fun onRequestLocationFinished(allowed: Boolean) {
        locationPermissionCallback?.let { it(allowed) }
    }
}

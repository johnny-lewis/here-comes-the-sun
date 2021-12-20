package au.com.lexicon.herecomesthesun.service

import android.content.Context
import android.location.Location
import au.com.lexicon.herecomesthesun.domain.model.GeoLocationData
import au.com.lexicon.herecomesthesun.domain.service.GeoLocation
import au.com.lexicon.herecomesthesun.domain.service.Permission
import com.google.android.gms.location.LocationServices
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GeoLocationService(
    private val context: Context,
    private val permission: Permission
) : GeoLocation {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressWarnings("MissingPermission")
    override suspend fun getCurrentLocation(): GeoLocationData? = suspendCoroutine { continuation ->
        if (!permission.hasLocationPermission()) {
            continuation.resume(null)
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                continuation.resume(
                    GeoLocationData(
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                )
            } ?: continuation.resume(null)
        }
    }
}

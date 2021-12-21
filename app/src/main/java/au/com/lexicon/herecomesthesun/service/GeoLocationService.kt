package au.com.lexicon.herecomesthesun.service

import android.content.Context
import android.location.Geocoder
import android.location.Location
import au.com.lexicon.herecomesthesun.domain.model.GeoLocationData
import au.com.lexicon.herecomesthesun.domain.service.GeoLocation
import au.com.lexicon.herecomesthesun.domain.service.Permission
import com.google.android.gms.location.LocationServices
import timber.log.Timber
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GeoLocationService(
    context: Context,
    private val permission: Permission
) : GeoLocation {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val geocoder = Geocoder(context, Locale.getDefault())

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

    override fun getLocationFromPostCode(postcode: String): GeoLocationData? =
        try {
            geocoder.getFromLocationName(postcode, 1).let { result ->
                result.firstOrNull()?.let {
                    GeoLocationData(
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                }
            }
        } catch (e: Exception) {
            Timber.e("Failed to get location from postcode: $postcode", e)
            null
        }
}

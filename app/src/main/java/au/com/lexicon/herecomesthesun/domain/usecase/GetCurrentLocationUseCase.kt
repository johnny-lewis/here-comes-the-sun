package au.com.lexicon.herecomesthesun.domain.usecase

import au.com.lexicon.herecomesthesun.domain.model.GeoLocationData
import au.com.lexicon.herecomesthesun.domain.service.GeoLocation
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val geoLocation: GeoLocation
) {
    suspend operator fun invoke(): GeoLocationData? =
        geoLocation.getCurrentLocation()
}

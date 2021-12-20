package au.com.lexicon.herecomesthesun.domain.service

import au.com.lexicon.herecomesthesun.domain.model.GeoLocationData

interface GeoLocation {
    suspend fun getCurrentLocation(): GeoLocationData?
}

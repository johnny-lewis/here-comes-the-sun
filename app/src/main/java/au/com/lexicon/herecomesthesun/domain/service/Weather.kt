package au.com.lexicon.herecomesthesun.domain.service

import au.com.lexicon.herecomesthesun.domain.model.GeoLocationData
import au.com.lexicon.herecomesthesun.service.model.ServiceResult

interface Weather {
    suspend fun get7DayForecast(geoLocationData: GeoLocationData): ServiceResult<*, String>
}

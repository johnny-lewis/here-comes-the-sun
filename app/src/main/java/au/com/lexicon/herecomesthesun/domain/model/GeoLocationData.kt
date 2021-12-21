package au.com.lexicon.herecomesthesun.domain.model

data class GeoLocationData(
    val latitude: Double,
    val longitude: Double
) {
    override fun toString(): String =
        "$latitude, $longitude"
}

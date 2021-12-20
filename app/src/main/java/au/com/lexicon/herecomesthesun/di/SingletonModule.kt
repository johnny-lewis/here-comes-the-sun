package au.com.lexicon.herecomesthesun.di

import android.content.Context
import au.com.lexicon.herecomesthesun.di.NetworkModule.Companion.WEATHER_RETROFIT
import au.com.lexicon.herecomesthesun.domain.service.GeoLocation
import au.com.lexicon.herecomesthesun.domain.service.Permission
import au.com.lexicon.herecomesthesun.domain.service.Weather
import au.com.lexicon.herecomesthesun.service.GeoLocationService
import au.com.lexicon.herecomesthesun.service.PermissionService
import au.com.lexicon.herecomesthesun.service.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Singleton
    @Provides
    fun provideWeatherService(
        @Named(WEATHER_RETROFIT) retrofit: Retrofit
    ): Weather =
        WeatherService(
            retrofit = retrofit
        )

    @Singleton
    @Provides
    fun providePermissionService(): Permission =
        PermissionService()

    @Singleton
    @Provides
    fun provideGeoLocationService(
        @ApplicationContext context: Context,
        permission: Permission
    ): GeoLocation =
        GeoLocationService(
            context = context,
            permission = permission
        )
}

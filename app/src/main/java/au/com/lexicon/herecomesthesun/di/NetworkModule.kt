package au.com.lexicon.herecomesthesun.di

import au.com.lexicon.herecomesthesun.BuildConfig
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton
import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    companion object {
        private const val WEATHER_BASE_URL: String = "https://api.weatherapi.com/v1/"
        const val WEATHER_RETROFIT: String = "WEATHER_RETROFIT"
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder().build()

    @Singleton
    @Provides
    @Named(WEATHER_RETROFIT)
    fun provideRetrofit(
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .client(buildWeatherClient())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    private fun buildWeatherClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                var request: Request = chain.request()
                val url = request.url.newBuilder()
                    .addQueryParameter("key", BuildConfig.weatherApiKey)
                    .build()

                request = request.newBuilder()
                    .url(url)
                    .build()
                chain.proceed(request)
            }
            .build()
}

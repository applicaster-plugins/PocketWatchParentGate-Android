package com.applicaster.plugin.coppa.pocketwatch.data.service

import android.util.Base64
import com.applicaster.plugin.coppa.pocketwatch.data.service.api.UrbanAirshipAPI
import com.urbanairship.UAirship
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

interface ApiFactory {
    val urbanAirshipAPI: UrbanAirshipAPI
}

class ApiFactoryImpl(private val masterSecret: String) : ApiFactory {

    companion object {
        const val URBAN_AIRSHIP_URL = "https://go.urbanairship.com/api/"
    }

    private val httpClient by lazy {
        var authHeader = ""
        try {
            authHeader = UAirship.shared().airshipConfigOptions.run {
                Base64.encodeToString("$appKey:$masterSecret".toByteArray(), Base64.DEFAULT)
            }
        } catch (e: IllegalStateException) {
            Timber.i(e, "No Urbanairship initialized")
        }

        OkHttpClient.Builder()
            .addInterceptor {
                val original = it.request()
                val request = original.newBuilder()
                    .header("Accept", "application/vnd.urbanairship+json; version=3;")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Basic $authHeader".trim())
                    .method(original.method(), original.body())
                    .build()

                it.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val loggingInterceptor
        get() = HttpLoggingInterceptor { Timber.d(it) }.setLevel(HttpLoggingInterceptor.Level.BODY)

    private val converter by lazy { GsonConverterFactory.create() }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URBAN_AIRSHIP_URL)
            .client(httpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(converter)
            .build()
    }

    override val urbanAirshipAPI: UrbanAirshipAPI by lazy {
        retrofit
            .create(UrbanAirshipAPI::class.java)
    }
}

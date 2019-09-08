package com.example.sfweather.features.weatherDetails.services

import com.example.sfweather.BuildConfig
import com.example.sfweather.features.weatherDetails.models.OWResult
import io.reactivex.Single
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OWService {

    @GET("weather")
    fun findByCityName(@Query("q") cityName: String): Single<OWResult>

    @GET("weather")
    fun findByCityId(@Query("id") cityId: Int): Single<OWResult>

    companion object Factory {
        fun create(): OWService {
            val interceptor = object: Interceptor {
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    var request = chain.request()

                    val newUrl: HttpUrl = request.url.newBuilder().addQueryParameter("appId", BuildConfig.OPENWEATHER_API_TOKEN).build()

                    request = request.newBuilder()
                        .url(newUrl)
                        .build()

                    return chain.proceed(request)
                }
            }

            val okHttpClient = OkHttpClient().newBuilder().
                addInterceptor(interceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.OPENWEATHER_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit.create(OWService::class.java);
        }
    }
}
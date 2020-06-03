package com.demo.instacopy.feed.data
/*
* Created by hrank8t on 03-06-2020 - 16:08:38.
*/




import com.demo.instacopy.API_KEY
import com.demo.instacopy.BuildConfig
import com.demo.instacopy.feed.models.Photos
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface NetworkService {

    @GET("/photos/")
    fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Single<List<Photos>>

    companion object {
        private lateinit var retrofit: Retrofit
        private const val BASE_URL = "https://api.unsplash.com/"

        private val apiKeyInterceptor = Interceptor { chain: Interceptor.Chain ->
            val uID = API_KEY
            val request = chain.request().newBuilder().build()
            val originalHttpUrl = request.url()
            val url = originalHttpUrl
                .newBuilder()
                .addQueryParameter("client_id", uID)
                .build()
            val finalRequest = request.newBuilder().url(url).build()
            chain.proceed(finalRequest)
        }

        fun getService(): NetworkService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client: OkHttpClient
            client = if (BuildConfig.DEBUG) {
                OkHttpClient.Builder()
                    .addInterceptor(apiKeyInterceptor)
                    .addInterceptor(interceptor)
                    .build()
            } else {
                OkHttpClient.Builder()
                    .addInterceptor(apiKeyInterceptor)
                    .build()
            }

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(NetworkService::class.java)
        }
    }

}
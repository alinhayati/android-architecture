package com.digigene.android.moviefinder.viewmodel

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class NetworkService {
    private var _retrofit: Retrofit? = null
        get() {
            return if (field == null) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
                field = Retrofit.Builder().baseUrl("http://bechdeltest.com/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create()).client(client).build()
                field
            } else field
        }

    suspend fun fetchAddress(address: String): List<ResultEntity>? {
        return _retrofit?.create(AddressService::class.java)?.fetchLocationFromServer(address)
    }

}

interface AddressService {
    @GET("getMoviesByTitle")
    suspend fun fetchLocationFromServer(@Query("title") title: String): List<ResultEntity>
}
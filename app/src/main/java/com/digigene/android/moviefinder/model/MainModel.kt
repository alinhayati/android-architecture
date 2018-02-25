package com.digigene.android.moviefinder.model

import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainModel {
    private var mRetrofit: Retrofit? = null

    fun fetchAddress(address: String): Single<List<MainModel.ResultEntity>>? {
        if (mRetrofit == null) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
            mRetrofit = Retrofit.Builder().baseUrl("http://bechdeltest.com/api/v1/").addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(client).build()
        }
        return mRetrofit?.create(MainModel.AddressService::class.java)?.fetchLocationFromServer(address)
    }

    class ResultEntity(val title: String, val rating: String, val date: String, val year: String)
    interface AddressService {
        @GET("getMoviesByTitle")
        fun fetchLocationFromServer(@Query("title") title: String): Single<List<ResultEntity>>
    }

}
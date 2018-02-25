package com.digigene.android.moviefinder.model

import com.digigene.android.moviefinder.controller.MainController
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainModel(val controller: MainController) {
    private var mRetrofit: Retrofit? = null
    var mList = listOf<ResultEntity>()
    lateinit var httpException: HttpException
    private var compositeDisposable = CompositeDisposable()

    fun findAddress(address: String) {
        val disposable: Disposable = fetchAddress(address)!!.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : DisposableSingleObserver<List<ResultEntity>?>() {
            override fun onSuccess(t: List<ResultEntity>) {
                mList = t
                controller.notifyTheListIsReady()
            }

            override fun onError(e: Throwable) {
                httpException = e as HttpException
                controller.notifyThereIsErrorGettingTheList()
            }
        })
        compositeDisposable.add(disposable)
    }

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

    fun stopLoadingTheList() {
        compositeDisposable.clear()
    }
}
package com.digigene.android.moviefinder.presenter

import android.view.View
import android.widget.Toast
import com.digigene.android.moviefinder.MainActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainPresenter {
    private lateinit var mainView: MainActivity
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    infix fun hasView(mainActivity: MainActivity) {
        mainView = mainActivity
    }

    fun findAddress(address: String) {
        val disposable: Disposable = fetchAddress(address)!!.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : DisposableObserver<List<ResultEntity>?>() {
            override fun onNext(t: List<ResultEntity>) {
                mainView.hideProgressBar()
                updateRecyclerView(t)
            }

            override fun onStart() {
                showProgressBar()
            }

            override fun onComplete() {
            }

            override fun onError(e: Throwable) {
                mainView.main_activity_progress_bar.visibility = View.GONE
                Toast.makeText(mainView, "Error retrieving data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        })
        compositeDisposable.add(disposable)
    }

    private fun fetchAddress(address: String): Observable<List<ResultEntity>>? {
        if (mRetrofit == null) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
            mRetrofit = Retrofit.Builder().baseUrl("http://bechdeltest.com/api/v1/").addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(client).build()
        }
        return mRetrofit?.create(AddressService::class.java)?.fetchLocationFromServer(address)
    }


    private var mRetrofit: Retrofit? = null


    interface AddressService {
        @GET("getMoviesByTitle")
        fun fetchLocationFromServer(@Query("title") title: String): Observable<List<ResultEntity>>
    }

    class ResultEntity(val title: String, val rating: String, val date: String, val year: String)

}
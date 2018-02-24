package com.digigene.android.moviefinder.presenter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.digigene.android.moviefinder.DetailActivity
import com.digigene.android.moviefinder.MainActivity
import com.digigene.android.moviefinder.model.MainModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainPresenter {
    private lateinit var mainView: MainActivity
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val mainModel: MainModel = MainModel()

    infix fun hasView(mainActivity: MainActivity) {
        mainView = mainActivity
    }

    fun findAddress(address: String) {
        val disposable: Disposable = mainModel.fetchAddress(address)!!.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : DisposableObserver<List<MainModel.ResultEntity>?>() {
            override fun onNext(t: List<MainModel.ResultEntity>) {
                mainView.hideProgressBar()
                mainView.updateMovieList(t)
            }

            override fun onStart() {
                mainView.showProgressBar()
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

    infix fun doWhenClickIsMadeOn(item: MainModel.ResultEntity) {
        var bundle = Bundle()
        bundle.putString(DetailActivity.Constants.RATING, item.rating)
        bundle.putString(DetailActivity.Constants.TITLE, item.title)
        bundle.putString(DetailActivity.Constants.YEAR, item.year)
        bundle.putString(DetailActivity.Constants.DATE, item.date)
        var intent = Intent(mainView, DetailActivity::class.java)
        intent.putExtras(bundle)
        mainView.startActivity(intent)
    }

    infix fun fetchItemTextFrom(it: MainModel.ResultEntity): String {
        return "${it.year}: ${it.title}"
    }

    fun onStop() {
        compositeDisposable.clear()
    }

}
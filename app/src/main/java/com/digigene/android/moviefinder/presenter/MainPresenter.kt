package com.digigene.android.moviefinder.presenter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.digigene.android.easymvp.BasePresenterImpl
import com.digigene.android.moviefinder.DetailActivity
import com.digigene.android.moviefinder.model.MainModel
import com.digigene.android.moviefinder.view.MainViewImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_fragment_layout.*

class MainPresenterImpl : BasePresenterImpl<MainViewImpl, MainModel>() {
    private val mainModel: MainModel = MainModel()

    fun findAddress(address: String) {
        val disposable: Disposable = mainModel.fetchAddress(address)!!.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : DisposableSingleObserver<List<MainModel.ResultEntity>?>() {
            override fun onSuccess(t: List<MainModel.ResultEntity>) {
                mView.hideProgressBar()
                mView.updateMovieList(t)
            }

            override fun onStart() {
                mView.showProgressBar()
            }

            override fun onError(e: Throwable) {
                mView.main_fragment_progress_bar.visibility = View.GONE
                Toast.makeText(mView.activity, "Error retrieving data: ${e.message}", Toast.LENGTH_SHORT).show()
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
        var intent = Intent(mView.activity, DetailActivity::class.java)
        intent.putExtras(bundle)
        mView.startActivity(intent)
    }

    infix fun fetchItemTextFrom(it: MainModel.ResultEntity): String {
        return "${it.year}: ${it.title}"
    }

}
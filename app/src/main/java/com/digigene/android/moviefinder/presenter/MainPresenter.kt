package com.digigene.android.moviefinder.presenter

import com.digigene.android.easymvp.BasePresenterImpl
import com.digigene.android.moviefinder.model.MainModel
import com.digigene.android.moviefinder.view.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainPresenter : BasePresenterImpl<MainView, MainModel>() {
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
                mView.hideProgressBar()
                mView.showErrorMessage("Error retrieving data: ${e.message}")
            }
        })
        compositeDisposable.add(disposable)
    }

    infix fun fetchItemTextFrom(it: MainModel.ResultEntity): String {
        return "${it.year}: ${it.title}"
    }

    fun doWhenItemIsClicked(it: MainModel.ResultEntity) {
        mView.goToActivity(it)
    }

}
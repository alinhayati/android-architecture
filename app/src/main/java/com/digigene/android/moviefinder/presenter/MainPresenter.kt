package com.digigene.android.moviefinder.presenter

import com.digigene.android.moviefinder.MainActivity
import com.digigene.android.moviefinder.SchedulersWrapper
import com.digigene.android.moviefinder.model.MainModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver

class MainPresenter() {
    private lateinit var mainView: MainActivity
    private lateinit var mainModel: MainModel
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val schedulersWrapper = SchedulersWrapper()

    infix fun hasView(mainActivity: MainActivity) {
        mainView = mainActivity
    }

    constructor(mMainModel: MainModel) : this() {
        mainModel = mMainModel
    }

    fun findAddress(address: String) {
        val disposable: Disposable = mainModel.fetchAddress(address)!!.subscribeOn(schedulersWrapper.io()).observeOn(schedulersWrapper.main()).subscribeWith(object : DisposableSingleObserver<List<MainModel.ResultEntity>?>() {
            override fun onSuccess(t: List<MainModel.ResultEntity>) {
                mainView.hideProgressBar()
                mainView.updateMovieList(t)
            }

            override fun onStart() {
                mainView.showProgressBar()
            }

            override fun onError(e: Throwable) {
                mainView.hideProgressBar()
                mainView.showErrorMessage(e.message!!)
            }
        })
        compositeDisposable.add(disposable)
    }

    infix fun doWhenItemIsClicked(item: MainModel.ResultEntity) {
        mainView.goToDetailActivity(item)
    }

    infix fun fetchItemTextFrom(it: MainModel.ResultEntity): String {
        return "${it.year}: ${it.title}"
    }

    fun onStop() {
        compositeDisposable.clear()
    }

}
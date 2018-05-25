package com.digigene.android.moviefinder.presenter

import com.digigene.android.moviefinder.SchedulersWrapper
import com.digigene.android.moviefinder.model.MainModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException

class MainViewModel() {
    lateinit var resultListObservable: PublishSubject<List<String>>
    lateinit var resultListObservable: PublishSubject<List<String>>
    lateinit var resultListErrorObservable: PublishSubject<HttpException>
    lateinit var itemObservable: PublishSubject<MainModel.ResultEntity>
    private lateinit var entityList: List<MainModel.ResultEntity>
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var mainModel: MainModel
    private val schedulersWrapper = SchedulersWrapper()

    constructor(mMainModel: MainModel) : this() {
        mainModel = mMainModel
        resultListObservable = PublishSubject.create()
        resultListErrorObservable = PublishSubject.create()
        itemObservable = PublishSubject.create()
    }

    fun findAddress(address: String) {
        val disposable: Disposable = mainModel.fetchAddress(address)!!.subscribeOn(schedulersWrapper.io()).observeOn(schedulersWrapper.main()).subscribeWith(object : DisposableSingleObserver<List<MainModel.ResultEntity>?>() {
            override fun onSuccess(t: List<MainModel.ResultEntity>) {
                entityList = t
                resultListObservable.onNext(fetchItemTextFrom(t))
            }

            override fun onError(e: Throwable) {
                resultListErrorObservable.onNext(e as HttpException)
            }
        })
        compositeDisposable.add(disposable)
    }

    fun cancelNetworkConnections() {
        compositeDisposable.clear()
    }

    private fun fetchItemTextFrom(it: List<MainModel.ResultEntity>): ArrayList<String> {
        val li = arrayListOf<String>()
        for (resultEntity in it) {
            li.add("${resultEntity.year}: ${resultEntity.title}")
        }
        return li
    }

    fun doOnItemClick(position: Int) {
        itemObservable.onNext(entityList[position])
    }

}
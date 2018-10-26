package com.digigene.android.moviefinder.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.digigene.android.moviefinder.SchedulersWrapper
import com.digigene.android.moviefinder.model.MainModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException

class MainViewModel() : ViewModel() {
    val resultListObservable: BehaviorSubject<List<String>> = BehaviorSubject.create()
    val resultListErrorObservable: BehaviorSubject<HttpException> = BehaviorSubject.create()
    val itemObservable: PublishSubject<MainModel.ResultEntity> = PublishSubject.create()
    val viewResultListObservable = ObservableField<List<MainModel.ResultEntity>>()
    val viewErrorObservable = ObservableField<HttpException>()
    private lateinit var entityList: List<MainModel.ResultEntity>
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    lateinit var mainModel: MainModel
    private val schedulersWrapper = SchedulersWrapper()

    fun findAddress(address: String) {
        val disposable: Disposable = mainModel.fetchAddress(address)!!.subscribeOn(schedulersWrapper.io()).observeOn(schedulersWrapper.main()).subscribeWith(object : DisposableSingleObserver<List<MainModel.ResultEntity>?>() {
            override fun onSuccess(t: List<MainModel.ResultEntity>) {
                entityList = t
                resultListObservable.onNext(fetchItemTextFrom(t))
                viewResultListObservable.set(t)
            }

            override fun onError(e: Throwable) {
                resultListErrorObservable.onNext(e as HttpException)
                viewErrorObservable.set(e)
            }
        })
        compositeDisposable.add(disposable)
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
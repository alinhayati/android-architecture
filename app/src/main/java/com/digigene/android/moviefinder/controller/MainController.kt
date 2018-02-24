package com.digigene.android.moviefinder.controller

import android.content.Intent
import android.os.Bundle
import com.digigene.android.moviefinder.DetailActivity
import com.digigene.android.moviefinder.MainActivity
import com.digigene.android.moviefinder.model.MainModel

class MainController {
    private lateinit var mainView: MainActivity
    private lateinit var mainModel: MainModel

    infix fun hasView(mainActivity: MainActivity) {
        mainView = mainActivity
    }

    fun findAddress(address: String) {
        mainView.notifyTheListIsAboutToStartLoading()
        mainModel.findAddress(address)
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

    fun onStop() {
        mainModel.stopLoadingTheList()
    }

    fun notifyTheListIsReady() {
        mainView.notifyToGetTheListFromTheModel()
    }

    fun notifyThereIsErrorGettingTheList() {
        mainView.notifyToGetTheErrorFromTheModel()
    }

    infix fun hasModel(mainModel: MainModel) {
        this.mainModel = mainModel
    }

}
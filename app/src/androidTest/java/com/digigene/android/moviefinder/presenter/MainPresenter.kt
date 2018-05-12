package com.digigene.android.moviefinder.presenter

import com.digigene.android.easymvp.BasePresenterImpl
import com.digigene.android.moviefinder.model.MainModel
import com.digigene.android.moviefinder.view.MainView

class MainPresenter : BasePresenterImpl<MainView>() {
    var actionString = ""

    fun findAddress(address: String) {
        actionString = "findAddressClicked"
    }

    fun doWhenItemIsClicked(resultEntity: MainModel.ResultEntity) {
        actionString = "recyclerviewItemClicked"
    }

    infix fun fetchItemTextFrom(it: MainModel.ResultEntity): String {
        return ""
    }

}
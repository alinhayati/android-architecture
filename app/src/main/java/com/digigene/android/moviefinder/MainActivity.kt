package com.digigene.android.moviefinder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.digigene.android.moviefinder.presenter.MainPresenter
import com.digigene.android.moviefinder.view.MainView

class MainActivity : AppCompatActivity() {

    private lateinit var mMainView: MainView
    private lateinit var mMainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        mMainPresenter = MainPresenter()
        mMainView = MainView()
        mMainView.mPresenter = mMainPresenter

        fragmentManager.beginTransaction().add(R.id.main_activity_holder, mMainView).commit()
    }
}

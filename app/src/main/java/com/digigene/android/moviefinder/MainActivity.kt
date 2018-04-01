package com.digigene.android.moviefinder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.digigene.android.moviefinder.view.MainViewImpl

class MainActivity : AppCompatActivity() {

    private lateinit var mMainView: MainViewImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        mMainView = MainViewImpl()
        fragmentManager.beginTransaction().add(R.id.main_activity_holder, mMainView).commit()
    }

}

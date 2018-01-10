package com.digigene.android.moviefinder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.digigene.android.moviefinder.MainActivity.Constants.MOVIE_INFO
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.getBundleExtra(MOVIE_INFO)
        setContentView(R.layout.activity_detail)
        detail_rating.text = bundle.getString(MainActivity.Constants.RATING)
        detail_title.text = bundle.getString(MainActivity.Constants.TITLE)
        detail_year.text = bundle.getString(MainActivity.Constants.YEAR)
        detail_date.text = bundle.getString(MainActivity.Constants.DATE)
    }
}
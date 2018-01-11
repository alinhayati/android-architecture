package com.digigene.android.moviefinder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        setContentView(R.layout.activity_detail)
        detail_rating.text = "${MainActivity.Constants.RATING.capitalize()}: ${bundle.getString(MainActivity.Constants.RATING)}"
        detail_title.text = "${MainActivity.Constants.TITLE.capitalize()}: ${bundle.getString(MainActivity.Constants.TITLE)}"
        detail_year.text = "${MainActivity.Constants.YEAR.capitalize()}: ${bundle.getString(MainActivity.Constants.YEAR)}"
        detail_date.text = "${MainActivity.Constants.DATE.capitalize()}: ${bundle.getString(MainActivity.Constants.DATE)}"
    }
}
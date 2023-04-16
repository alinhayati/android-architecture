package com.digigene.android.moviefinder

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        setContentView(R.layout.activity_detail)
        findViewById<TextView>(R.id.detail_rating).text =
            "${
                MainActivity.Constants.RATING.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
            }: ${bundle?.getString(MainActivity.Constants.RATING)}"
        findViewById<TextView>(R.id.detail_title).text =
            "${
                MainActivity.Constants.TITLE.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
            }: ${bundle?.getString(MainActivity.Constants.TITLE)}"
        findViewById<TextView>(R.id.detail_year).text =
            "${
                MainActivity.Constants.YEAR.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
            }: ${bundle?.getString(MainActivity.Constants.YEAR)}"
        findViewById<TextView>(R.id.detail_date).text =
            "${
                MainActivity.Constants.DATE.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
            }: ${bundle?.getString(MainActivity.Constants.DATE)}"
    }
}
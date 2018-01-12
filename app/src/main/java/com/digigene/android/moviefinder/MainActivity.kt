package com.digigene.android.moviefinder

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.digigene.android.moviefinder.MainActivity.Constants.DATE
import com.digigene.android.moviefinder.MainActivity.Constants.RATING
import com.digigene.android.moviefinder.MainActivity.Constants.TITLE
import com.digigene.android.moviefinder.MainActivity.Constants.YEAR
import com.digigene.android.moviefinder.presenter.MainPresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : AppCompatActivity() {
    object Constants {
        const val RATING = "rating"
        const val TITLE = "title"
        const val YEAR = "year"
        const val DATE = "date"
    }

    private lateinit var mainPresenter: MainPresenter
    private lateinit var addressAdapter: AddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainPresenter = MainPresenter()
        mainPresenter hasView this
        loadView()
        respondToClicks()
    }

    private fun loadView() {
        setContentView(R.layout.activity_main)
        addressAdapter = AddressAdapter()
        main_activity_recyclerView.adapter = addressAdapter
    }

    private fun respondToClicks() {
        main_activity_button.setOnClickListener({ mainPresenter.findAddress(main_activity_editText.text.toString()) })
        addressAdapter whenItsItemIsClicked {
            var bundle = Bundle()
            bundle.putString(RATING, it.rating)
            bundle.putString(TITLE, it.title)
            bundle.putString(YEAR, it.year)
            bundle.putString(DATE, it.date)
            var intent = Intent(this, DetailActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        hideProgressBar()
    }

    private fun showProgressBar() {
        main_activity_progress_bar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        main_activity_progress_bar.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    private fun updateRecyclerView(t: List<ResultEntity>) {
        addressAdapter.updateList(t)
        addressAdapter.notifyDataSetChanged()
    }

    class AddressAdapter() : RecyclerView.Adapter<AddressAdapter.Holder>() {
        var mList: List<ResultEntity> = arrayListOf()
        private lateinit var mOnClick: (item: ResultEntity) -> Unit

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
            val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.itemView.item_textView.text = "${mList[position].year}: ${mList[position].title}"
            holder.itemView.setOnClickListener { mOnClick(mList[position]) }
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        infix fun whenItsItemIsClicked(onClick: (item: ResultEntity) -> Unit) {
            this.mOnClick = onClick
        }

        fun updateList(list: List<ResultEntity>) {
            mList = list
        }

        class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView)
    }

}

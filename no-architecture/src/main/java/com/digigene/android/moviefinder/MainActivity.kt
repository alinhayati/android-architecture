package com.digigene.android.moviefinder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.digigene.android.moviefinder.MainActivity.Constants.DATE
import com.digigene.android.moviefinder.MainActivity.Constants.RATING
import com.digigene.android.moviefinder.MainActivity.Constants.TITLE
import com.digigene.android.moviefinder.MainActivity.Constants.YEAR
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
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

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var addressAdapter: AddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addressAdapter = AddressAdapter(onClick = { item ->
            val bundle = Bundle()
            bundle.putString(RATING, item.rating)
            bundle.putString(TITLE, item.title)
            bundle.putString(YEAR, item.year)
            bundle.putString(DATE, item.date)
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        })
        findViewById<RecyclerView>(R.id.main_activity_recyclerView).adapter = addressAdapter
        respondToClicks()
    }

    private fun respondToClicks() {
        findViewById<Button>(R.id.main_activity_button).setOnClickListener {
            findAddress(
                findViewById<TextView>(R.id.main_activity_editText).text.toString()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        hideProgressBar()
    }

    private fun findAddress(address: String) {
        val disposable: Disposable = fetchAddress(address)!!.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : DisposableObserver<List<ResultEntity>?>() {
            override fun onNext(t: List<ResultEntity>) {
                hideProgressBar()
                updateRecyclerView(t)
            }

            override fun onStart() {
                showProgressBar()
            }

            override fun onComplete() {
            }

            override fun onError(e: Throwable) {
                findViewById<ProgressBar>(R.id.main_activity_progress_bar).visibility = View.GONE
                Toast.makeText(this@MainActivity, "Error retrieving data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        })
        compositeDisposable.add(disposable)
    }

    private fun showProgressBar() {
        findViewById<ProgressBar>(R.id.main_activity_progress_bar).visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        findViewById<ProgressBar>(R.id.main_activity_progress_bar).visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    private fun updateRecyclerView(t: List<ResultEntity>) {
        addressAdapter.updateList(t)
        addressAdapter.notifyDataSetChanged()
    }

    class AddressAdapter(val onClick: (item: ResultEntity) -> Unit) : RecyclerView.Adapter<AddressAdapter.Holder>() {
        var mList: List<ResultEntity> = arrayListOf()


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.itemView.findViewById<TextView>(R.id.item_textView).text = "${mList[position].year}: ${mList[position].title}"
            holder.itemView.setOnClickListener { onClick(mList[position]) }
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        fun updateList(list: List<ResultEntity>) {
            mList = list
        }

        class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!)
    }


    private var mRetrofit: Retrofit? = null

    private fun fetchAddress(address: String): Observable<List<ResultEntity>>? {
        if (mRetrofit == null) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
            mRetrofit = Retrofit.Builder().baseUrl("http://bechdeltest.com/api/v1/").addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).client(client).build()
        }
        return mRetrofit?.create(AddressService::class.java)?.fetchLocationFromServer(address)
    }

    interface AddressService {
        @GET("getMoviesByTitle")
        fun fetchLocationFromServer(@Query("title") title: String): Observable<List<ResultEntity>>
    }

    class ResultEntity(val title: String, val rating: String, val date: String, val year: String)
}

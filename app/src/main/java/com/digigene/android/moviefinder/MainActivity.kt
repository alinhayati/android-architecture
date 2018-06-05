package com.digigene.android.moviefinder

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.digigene.android.moviefinder.model.MainModel
import com.digigene.android.moviefinder.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mAddressAdapter: AddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainViewModel = MainViewModel(MainModel())
        loadView()
        respondToClicks()
        listenToObservables()
    }

    private fun listenToObservables() {
        mMainViewModel.getItemObservable().observe(this, Observer { goToDetailActivity(it!!) })
        mMainViewModel.getResultListObservable().observe(this, Observer {
            hideProgressBar()
            updateMovieList(it!!)
        })
        mMainViewModel.getResultListErrorObservable().observe(this, Observer {
            hideProgressBar()
            showErrorMessage(it!!.message())
        })
    }

    private fun loadView() {
        setContentView(R.layout.activity_main)
        mAddressAdapter = AddressAdapter()
        main_activity_recyclerView.adapter = mAddressAdapter
    }

    private fun respondToClicks() {
        main_activity_button.setOnClickListener({
            showProgressBar()
            mMainViewModel.findAddress(main_activity_editText.text.toString())
        })
        mAddressAdapter setItemClickMethod {
            mMainViewModel.doOnItemClick(it)
        }
    }

    fun showProgressBar() {
        main_activity_progress_bar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        main_activity_progress_bar.visibility = View.GONE
    }

    fun showErrorMessage(errorMsg: String) {
        Toast.makeText(this, "Error retrieving data: $errorMsg", Toast.LENGTH_SHORT).show()
    }

    fun updateMovieList(t: List<String>) {
        mAddressAdapter.updateList(t)
        mAddressAdapter.notifyDataSetChanged()
    }

    fun goToDetailActivity(item: MainModel.ResultEntity) {
        var bundle = Bundle()
        bundle.putString(DetailActivity.Constants.RATING, item.rating)
        bundle.putString(DetailActivity.Constants.TITLE, item.title)
        bundle.putString(DetailActivity.Constants.YEAR, item.year)
        bundle.putString(DetailActivity.Constants.DATE, item.date)
        var intent = Intent(this, DetailActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    class AddressAdapter : RecyclerView.Adapter<AddressAdapter.Holder>() {
        var mList: List<String> = arrayListOf()
        private lateinit var mOnClick: (position: Int) -> Unit

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
            val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.itemView.item_textView.text = mList[position]
            holder.itemView.setOnClickListener { mOnClick(position) }
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        infix fun setItemClickMethod(onClick: (position: Int) -> Unit) {
            this.mOnClick = onClick
        }

        fun updateList(list: List<String>) {
            mList = list
        }

        class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView)
    }

}

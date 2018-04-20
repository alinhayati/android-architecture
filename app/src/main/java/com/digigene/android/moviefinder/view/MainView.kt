package com.digigene.android.moviefinder.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.digigene.android.easymvp.BaseViewImpl
import com.digigene.android.moviefinder.DetailActivity
import com.digigene.android.moviefinder.R
import com.digigene.android.moviefinder.model.MainModel
import com.digigene.android.moviefinder.presenter.MainPresenter
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.main_fragment_layout.*

class MainView : BaseViewImpl<MainPresenter>() {
    private lateinit var addressAdapter: AddressAdapter

    override fun introducePresenter(): MainPresenter {
        return MainPresenter()
    }

    override fun getFragmentLayout(): Int {
        return R.layout.main_fragment_layout
    }

    override fun setListeners() {
        main_fragment_button.setOnClickListener({ mPresenter.findAddress(main_fragment_editText.text.toString()) })
        addressAdapter setItemClickMethod {
            mPresenter.doWhenItemIsClicked(it)
        }
        addressAdapter.setItemShowMethod { mPresenter fetchItemTextFrom it }
    }

    override fun introduceViewElements() {
        addressAdapter = AddressAdapter()
        main_fragment_recyclerView.adapter = addressAdapter
    }

    fun showProgressBar() {
        main_fragment_progress_bar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        main_fragment_progress_bar.visibility = View.GONE
    }

    fun updateMovieList(t: List<MainModel.ResultEntity>) {
        addressAdapter.updateList(t)
        addressAdapter.notifyDataSetChanged()
    }

    fun showErrorMessage(s: String) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show()
    }

    infix fun goToActivity(item: MainModel.ResultEntity) {
        var bundle = Bundle()
        bundle.putString(DetailActivity.Constants.RATING, item.rating)
        bundle.putString(DetailActivity.Constants.TITLE, item.title)
        bundle.putString(DetailActivity.Constants.YEAR, item.year)
        bundle.putString(DetailActivity.Constants.DATE, item.date)
        var intent = Intent(activity, DetailActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    class AddressAdapter : RecyclerView.Adapter<AddressAdapter.Holder>() {
        var mList: List<MainModel.ResultEntity> = arrayListOf()
        private lateinit var mOnClick: (item: MainModel.ResultEntity) -> Unit
        private lateinit var mOnShowItem: (item: MainModel.ResultEntity) -> String

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
            val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.itemView.item_textView.text = mOnShowItem(mList[position])
            holder.itemView.setOnClickListener { mOnClick(mList[position]) }
        }

        override fun getItemCount(): Int {
            return mList.size
        }

        infix fun setItemClickMethod(onClick: (item: MainModel.ResultEntity) -> Unit) {
            this.mOnClick = onClick
        }

        infix fun setItemShowMethod(onShowItem: (item: MainModel.ResultEntity) -> String) {
            this.mOnShowItem = onShowItem
        }

        fun updateList(list: List<MainModel.ResultEntity>) {
            mList = list
        }

        class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView)
    }

}
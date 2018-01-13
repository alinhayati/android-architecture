package com.digigene.android.moviefinder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.digigene.android.moviefinder.model.MainModel
import com.digigene.android.moviefinder.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*

class MainActivity : AppCompatActivity() {

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
        addressAdapter whenItemIsClicked {
            mainPresenter doWhenClickIsMadeOn it
        }
    }

    override fun onResume() {
        super.onResume()
        hideProgressBar()
    }

    fun showProgressBar() {
        main_activity_progress_bar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        main_activity_progress_bar.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        mainPresenter.onStop()
    }

    fun updateMovieList(t: List<MainModel.ResultEntity>) {
        addressAdapter.updateList(t)
        addressAdapter.notifyDataSetChanged()
    }

    class AddressAdapter : RecyclerView.Adapter<AddressAdapter.Holder>() {
        var mList: List<MainModel.ResultEntity> = arrayListOf()
        private lateinit var mOnClick: (item: MainModel.ResultEntity) -> Unit

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

        infix fun whenItemIsClicked(onClick: (item: MainModel.ResultEntity) -> Unit) {
            this.mOnClick = onClick
        }

        fun updateList(list: List<MainModel.ResultEntity>) {
            mList = list
        }

        class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView)
    }

}

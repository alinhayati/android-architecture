package com.digigene.android.moviefinder

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.digigene.android.moviefinder.controller.MainController
import com.digigene.android.moviefinder.model.MainModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var mMainController: MainController
    private lateinit var mMainModel: MainModel
    private lateinit var addressAdapter: AddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainController = MainController()
        mMainModel = MainModel(mMainController)
        mMainController hasView this
        mMainController hasModel mMainModel
        loadView()
        respondToClicks()
    }

    private fun loadView() {
        setContentView(R.layout.activity_main)
        addressAdapter = AddressAdapter()
        main_activity_recyclerView.adapter = addressAdapter
    }

    private fun respondToClicks() {
        main_activity_button.setOnClickListener({ mMainController.findAddress(main_activity_editText.text.toString()) })
        addressAdapter setItemClickMethod {
            mMainController doWhenClickIsMadeOn it
        }
        addressAdapter.setItemShowMethod { fetchItemText(it) }
    }

    fun fetchItemText(it: MainModel.ResultEntity): String {
        return "${it.year}: ${it.title}"
    }

    override fun onStop() {
        super.onStop()
        mMainController.onStop()
    }

    private fun updateMovieList(t: List<MainModel.ResultEntity>) {
        addressAdapter.updateList(t)
        addressAdapter.notifyDataSetChanged()
    }

    fun showResult() {
        updateMovieList(mMainModel.mList)
    }

    fun showError() {
        showToast(this, getString(R.string.error_getting_results, mMainModel.httpException.message))
    }

    fun showProgressBar() {
        main_activity_progress_bar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        main_activity_progress_bar.visibility = View.GONE
    }

    private fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
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

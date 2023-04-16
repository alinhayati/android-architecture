@file:OptIn(ExperimentalMaterial3Api::class)

package com.digigene.android.moviefinder

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.digigene.android.moviefinder.MainActivity.Constants.DATE
import com.digigene.android.moviefinder.MainActivity.Constants.RATING
import com.digigene.android.moviefinder.MainActivity.Constants.TITLE
import com.digigene.android.moviefinder.MainActivity.Constants.YEAR
import com.digigene.android.moviefinder.repository.Repository
import com.digigene.android.moviefinder.viewmodel.MainViewModel
import com.digigene.android.moviefinder.viewmodel.NetworkService
import com.digigene.android.moviefinder.viewmodel.ResultEntity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    object Constants {
        const val RATING = "rating"
        const val TITLE = "title"
        const val YEAR = "year"
        const val DATE = "date"
    }

    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var searchButton: Button
    private val viewmodel = MainViewModel(Repository(NetworkService()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById<ProgressBar>(R.id.main_activity_progress_bar)
        recyclerView = findViewById<RecyclerView>(R.id.main_activity_recyclerView)
        searchButton = findViewById<Button>(R.id.main_activity_button)
        listenToViewModel()

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
        recyclerView.adapter = addressAdapter
        respondToClicks()
    }

    private fun listenToViewModel() {
        listenWithLifecycleAware<List<ResultEntity>> {
            viewmodel.list.collect {
                with(addressAdapter) {
                    updateList(it)
                    notifyDataSetChanged()
                }
            }
        }
        listenWithLifecycleAware<Boolean> {
            viewmodel.showProgressBar.collect {
                progressBar.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
        listenWithLifecycleAware<String> {
            viewmodel.error.collect {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun <T> listenWithLifecycleAware(listenable: suspend () -> Unit) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                listenable()
            }
        }
    }

    @Composable
    fun buildMainContent() {
        return Column {
            TextField(value = "Some movie name", onValueChange = {})


        }
    }

    private fun respondToClicks() {
        searchButton.setOnClickListener {
            viewmodel.fetchAddress(
                findViewById<TextView>(R.id.main_activity_editText).text.toString()
            )
        }
    }

    class AddressAdapter(val onClick: (item: ResultEntity) -> Unit) :
        RecyclerView.Adapter<AddressAdapter.Holder>() {
        var mList: List<ResultEntity> = arrayListOf()


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.itemView.findViewById<TextView>(R.id.item_textView).text =
                "${mList[position].year}: ${mList[position].title}"
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


}

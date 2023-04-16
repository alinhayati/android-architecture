@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.digigene.android.moviefinder

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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

    private val viewmodel = MainViewModel(Repository(NetworkService()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            buildMainContent()
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
        var list: List<ResultEntity>? by remember {
            mutableStateOf(null)
        }
        var showProgressBar: Boolean by remember {
            mutableStateOf(false)
        }
        var searchTerm: String by remember {
            mutableStateOf("")
        }
        return Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = Dp(8f)),
            content = {
                if (showProgressBar) Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                    content = {
                        CircularProgressIndicator()
                    })
                TextField(
                    modifier = Modifier.align(CenterHorizontally),
                    value = searchTerm,
                    onValueChange = {
                        searchTerm = it
                    })
                Button(
                    modifier = Modifier.align(CenterHorizontally),
                    onClick = { if (searchTerm.isNotBlank()) viewmodel.fetchAddress(searchTerm) }) {
                    Text(text = "FIND")
                }
                list?.let { buildListView(it) }
                listenWithLifecycleAware<List<ResultEntity>> {
                    viewmodel.list.collect { list = it }
                }
                listenWithLifecycleAware<Boolean> {
                    viewmodel.showProgressBar.collect { showProgressBar = it }
                }
                listenWithLifecycleAware<String> {
                    viewmodel.error.collect {
                        Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    @Composable
    fun buildListView(list: List<ResultEntity>) {
        return LazyColumn(content = {
            items(list) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = Dp(16f), vertical = Dp(16f))
                        .clickable {
                            val bundle = Bundle()
                            bundle.putString(RATING, it.rating)
                            bundle.putString(TITLE, it.title)
                            bundle.putString(YEAR, it.year)
                            bundle.putString(DATE, it.date)
                            val intent = Intent(this@MainActivity, DetailActivity::class.java)
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }, text = "${it.year}: ${it.title}"
                )
            }
        })
    }

    @Composable
    @Preview
    fun previewBuildListView() {
        return buildListView(
            list = listOf(
                ResultEntity("dummy 1", "2", "1990", "1990"),
                ResultEntity("dummy 2", "3", "2000", "2000")
            )
        )
    }
}

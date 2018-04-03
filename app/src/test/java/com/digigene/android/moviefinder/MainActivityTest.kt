package com.digigene.android.moviefinder

import com.digigene.android.moviefinder.model.MainModel
import com.digigene.android.moviefinder.presenter.MainPresenter
import com.nhaarman.mockito_kotlin.inOrder
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MainActivityTest {
    @InjectMocks
    private lateinit var mainActivity: MainActivity
    @Mock
    private lateinit var mainPresenter: MainPresenter
    @Mock
    private lateinit var addressAdapter: MainActivity.AddressAdapter

    @Before
    fun setUp() {
        mainActivity = MainActivity()
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun updateMovieList() {
        val movieList = ArrayList<MainModel.ResultEntity>()
        mainActivity.updateMovieList(movieList)
        val order= inOrder(addressAdapter,addressAdapter)
        order.verify(addressAdapter).updateList(movieList)
        order.verify(addressAdapter).notifyDataSetChanged()
    }
}
package com.digigene.android.moviefinder

import com.digigene.android.moviefinder.controller.MainController
import com.digigene.android.moviefinder.model.MainModel
import com.nhaarman.mockito_kotlin.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MainActivityTest {
    @Mock
    private lateinit var addressAdapter: MainActivity.AddressAdapter
    @Mock
    private lateinit var mMainController: MainController
    @Mock
    private lateinit var mMainModel: MainModel
    @InjectMocks
    private lateinit var mMainView: MainActivity

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun fetchItemText() {
        val text = mMainView.fetchItemText(MainModel.ResultEntity("a movie title", "a rating", "a date", "a year"))
        assertThat(text, equalTo("a year: a movie title"))
    }

    @Test
    fun showResult() {
        mMainView.showResult()
        verify(addressAdapter).updateList(mMainModel.mList)
    }

    @Test
    fun showError() {
        // cannot be unit tested in this architecture
    }

}
package com.digigene.android.moviefinder

import com.digigene.android.moviefinder.controller.MainController
import com.digigene.android.moviefinder.model.MainModel
import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Spy

class MainActivityTest {
    @Mock
    private lateinit var mMainController: MainController
    @Mock
    private lateinit var mMainModel: MainModel
    @Mock
    private lateinit var addressAdapter: MainActivity.AddressAdapter
    @InjectMocks
    private lateinit var mainView: MainActivity
    @Spy
    private lateinit var mMainView: MainActivity

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mMainView = spy(mainView)
    }

    @Test
    fun notifyToGetTheListFromTheModel() {
        doNothing().`when`(mMainView).hideProgressBar()
        mMainView.notifyToGetTheListFromTheModel()
        verify(mMainModel).mList
    }

    @Test
    fun fetchItemText() {
        val text = mainView.fetchItemText(MainModel.ResultEntity("a movie title", "a rating", "a date", "a year"))
        assertThat(text,equalTo("a year: a movie title"))
    }

}
package com.digigene.android.moviefinder.model

import com.digigene.android.moviefinder.controller.MainController
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit

class MainModelTest {
    @Mock
    private lateinit var mRetrofit: Retrofit
    @Mock
    private lateinit var controller: MainController
    @InjectMocks
    private lateinit var mainModel: MainModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun findAddress_test1() {
        var testResponse = Single.just(listOf<MainModel.ResultEntity>())
        doReturn(testResponse).`when`(mRetrofit)?.create(MainModel.AddressService::class.java)?.fetchLocationFromServer(any<String>())
        mainModel.findAddress("any")
        verify(controller).notifyTheListIsReady()
    }

}

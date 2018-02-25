package com.digigene.android.moviefinder.controller

import com.digigene.android.moviefinder.MainActivity
import com.digigene.android.moviefinder.model.MainModel
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MainControllerTest {
    //    private lateinit var mainController: MainController
    @Mock
    private lateinit var mainView: MainActivity
    @Mock
    private lateinit var mainModel: MainModel
    @InjectMocks
    private lateinit var mainController: MainController


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        //        mainView = mock<MainActivity>{}
        //        mainModel = mock<MainModel>{}
        //        mainController = MainController()


    }

    @Test
    fun findAddress_test1() {
        var testObservable = Single.just(listOf<MainModel.ResultEntity>())
        doReturn(testObservable).`when`(mainModel).fetchAddress(any())
        doNothing().`when`(mainView).notifyTheListIsAboutToStartLoading()
        mainController.findAddress("some")
        verify(mainView).notifyTheListIsAboutToStartLoading()
        verify(mainView).notifyToGetTheListFromTheModel()


    }

}


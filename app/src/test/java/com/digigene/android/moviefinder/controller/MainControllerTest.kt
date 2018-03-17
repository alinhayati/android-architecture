package com.digigene.android.moviefinder.controller

import com.digigene.android.moviefinder.MainActivity
import com.digigene.android.moviefinder.model.MainModel
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MainControllerTest {
    @Mock
    private lateinit var mainView: MainActivity
    @Mock
    private lateinit var mainModel: MainModel
    @InjectMocks
    private lateinit var mainController: MainController

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun findAddress() {
        val movieString = "some keyword"
        mainController.findAddress(movieString)
        val argumentCaptor = argumentCaptor<String>()
        verify(mainModel).findAddress(argumentCaptor.capture())
        assertTrue(argumentCaptor.firstValue == movieString)
    }

    @Test
    fun onStop() {
        mainController.onStop()
        verify(mainModel).stopLoadingTheList()
    }

    @Test
    fun doWhenResultIsReady() {
        mainController.doWhenResultIsReady()
        verify(mainView).hideProgressBar()
        verify(mainView).showResult()
    }

    @Test
    fun doWhenThereIsErrorFetchingTheResult() {
        mainController.doWhenThereIsErrorFetchingTheResult()
        verify(mainView).hideProgressBar()
        verify(mainView).showError()
    }
}
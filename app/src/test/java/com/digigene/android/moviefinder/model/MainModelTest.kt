package com.digigene.android.moviefinder.model

import com.digigene.android.moviefinder.controller.MainController
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response

class MainModelTest {
    @Mock
    private lateinit var mainController: MainController
    @InjectMocks
    private lateinit var mainModel: MainModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun findAddress_whenResultIsSuccessful() {
        val mainModelSpy = spy(mainModel)
        val movieTitle = "any title"
        val successfulResponse = Single.just(listOf<MainModel.ResultEntity>())
        doReturn(successfulResponse).`when`(mainModelSpy).fetchAddress(movieTitle)
        mainModelSpy.findAddress(movieTitle)
        verify(mainController).doWhenResultIsReady()
    }

    @Test
    fun findAddress_whenThereIsError() {
        val mainModelSpy = spy(mainModel)
        val movieTitle = "any title"
        var errorResponse = Single.error<Response<HttpException>>(HttpException(generateErrorResponse()))
        doReturn(errorResponse).`when`(mainModelSpy).fetchAddress(movieTitle)
        mainModelSpy.findAddress(movieTitle)
        verify(mainController).doWhenThereIsErrorFetchingTheResult()
    }

    private fun generateErrorResponse(): Response<HttpException> {
        return Response.error(404, ResponseBody.create(MediaType.parse("application/json"), "Resource not found!"))
    }
}

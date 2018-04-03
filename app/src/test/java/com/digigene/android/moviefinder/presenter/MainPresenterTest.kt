package com.digigene.android.moviefinder.presenter

import com.digigene.android.moviefinder.MainActivity
import com.digigene.android.moviefinder.model.MainModel
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
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

class MainPresenterTest {
    @InjectMocks
    private lateinit var mainPresenter: MainPresenter
    @Mock
    private lateinit var mainView: MainActivity
    @Mock
    private lateinit var mainModel: MainModel

    @Before
    fun setUp() {
        mainPresenter = MainPresenter()
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun findAddress_whenResultIsSuccessful() {
        val address = "address"
        val dummyResult = listOf(MainModel.ResultEntity("title", "rating", "date", "year"))
        val observable = Single.just(dummyResult)
        doReturn(observable).`when`(mainModel).fetchAddress(address)
        mainPresenter.findAddress(address)
        verify(mainView).hideProgressBar()
        verify(mainView).updateMovieList(dummyResult)
    }

    @Test
    fun findAddress_whenResultIsNotSuccessful() {
        val address = "address"
        val error = HttpException(generateErrorResponse())
        val observable = Single.error<Response<HttpException>>(error)
        doReturn(observable).`when`(mainModel).fetchAddress(address)
        mainPresenter.findAddress(address)
        verify(mainView).hideProgressBar()
        verify(mainView).showErrorMessage(any())
    }

    private fun generateErrorResponse(): Response<HttpException> {
        return Response.error(404, ResponseBody.create(MediaType.parse("application/json"), "Resource not found!"))
    }

}
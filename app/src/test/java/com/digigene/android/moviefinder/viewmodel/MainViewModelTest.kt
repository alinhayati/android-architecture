package com.digigene.android.moviefinder.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.digigene.android.moviefinder.TestUtil.mockIt
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.HttpException
import retrofit2.Response

//@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        viewModel = MainViewModel()
    }

    @Test
    fun getResultListObservable() {
        val observer = mockIt<Observer<List<String>>>()
        viewModel.getResultListObservable().observeForever(observer)
        val testList = listOf<String>("a", "b", "c")
        viewModel.resultListObservable.value = testList
        verify(observer, times(1)).onChanged(testList)
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun getResultListErrorObservable() {
        val observer = mockIt<Observer<HttpException>>()
        viewModel.getResultListErrorObservable().observeForever(observer)
        val error = HttpException(generateErrorResponse())
        viewModel.resultListErrorObservable.value = error
        verify(observer, times(1)).onChanged(error)
        verifyNoMoreInteractions(observer)
    }

    private fun generateErrorResponse(): Response<HttpException> {
        return Response.error(404, ResponseBody.create(MediaType.parse("application/json"), "Resource not found!"))
    }

}
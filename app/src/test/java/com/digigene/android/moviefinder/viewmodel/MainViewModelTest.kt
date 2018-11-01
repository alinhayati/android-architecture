package com.digigene.android.moviefinder.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.digigene.android.moviefinder.SchedulersWrapper
import com.digigene.android.moviefinder.TestSchedulersWrapper
import com.digigene.android.moviefinder.TestUtil.mockIt
import com.digigene.android.moviefinder.model.MainModel
import io.reactivex.Single
import junit.framework.Assert
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    @Mock
    lateinit var mainModel: MainModel
    val schedulerWrapper: SchedulersWrapper = TestSchedulersWrapper()
    @InjectMocks
    val viewModel = MainViewModel(schedulerWrapper)
    val searchText = "some text"
    val testResult = listOf(MainModel.ResultEntity("title1", "rating1", "date1", "year1"), MainModel.ResultEntity("title2", "rating2", "date2", "year2"), MainModel.ResultEntity("title3", "rating3", "date3", "year3"))

    @Before
    fun setup() {
        reset(mainModel)
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

    @Test
    fun fetchAddress_whenResultIsAvailable() {
        val formattedTestResult = listOf("year1: title1", "year2: title2", "year3: title3")
        val resultObservable = Single.just(testResult)
        `when`(mainModel.fetchAddress(searchText)).thenReturn(resultObservable)
        viewModel.findAddress(searchText)
        val testObserver = mainModel.fetchAddress(searchText)?.test()
        testObserver?.assertValue(testResult)
        Assert.assertEquals(viewModel.entityList, testResult)
        Assert.assertEquals(viewModel.resultListObservable.value, formattedTestResult)
    }

    @Test
    fun fetchAddress_whenErrorFound() {
        val testError = HttpException(generateErrorResponse())
        val resultObservable = Single.error<List<MainModel.ResultEntity>>(testError)
        `when`(mainModel.fetchAddress(searchText)).thenReturn(resultObservable)
        viewModel.findAddress(searchText)
        val testObserver = mainModel.fetchAddress(searchText)?.test()
        testObserver?.assertError(testError)
        Assert.assertEquals(viewModel.resultListErrorObservable.value, testError)
    }

    private fun generateErrorResponse(): Response<HttpException> {
        return Response.error(404, ResponseBody.create(MediaType.parse("application/json"), "Resource not found!"))
    }

}
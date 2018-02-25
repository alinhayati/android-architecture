package com.digigene.android.moviefinder.model

import com.digigene.android.moviefinder.controller.MainController
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.Before
import org.junit.Test

class MainModelTest {
    private lateinit var mainModel: MainModel
    private lateinit var controller: MainController

    @Before
    fun setUp() {
        controller = mock()
        mainModel = MainModel(controller)
    }

    @Test
    fun findAddress_test1() {
        var testResponse = listOf<MainModel.ResultEntity>()
        doReturn(testResponse).`when`(mainModel).fetchAddress(any<String>())
        val testObserver = mainModel.fetchAddress(any<String>())!!.test()
        assert(mainModel.mList == testResponse)
    }

}

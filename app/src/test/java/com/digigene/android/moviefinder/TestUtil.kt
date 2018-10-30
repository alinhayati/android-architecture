package com.digigene.android.moviefinder

import org.mockito.ArgumentCaptor
import org.mockito.Mockito

object TestUtil {
    inline fun <reified T> mockIt() = Mockito.mock(T::class.java)
    inline fun <reified T> captorIt() = ArgumentCaptor.forClass(T::class.java)
}
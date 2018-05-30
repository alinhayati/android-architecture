package com.digigene.android.moviefinder.viewmodel

import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class MainViewModelTest {

    private fun generateErrorResponse(): Response<HttpException> {
        return Response.error(404, ResponseBody.create(MediaType.parse("application/json"), "Resource not found!"))
    }

}
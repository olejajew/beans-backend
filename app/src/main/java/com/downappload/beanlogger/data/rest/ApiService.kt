package com.downappload.beanlogger.data.rest

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("log") // URL для первого запроса
    fun sendLogs(@Body requestBody: RequestBody): Call<ResponseBody>

    @POST("dump") // URL для второго запроса
    fun sendDump(@Body requestBody: RequestBody): Call<ResponseBody>

}
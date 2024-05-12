package com.downappload.beanlogger.data.rest

import android.util.Log
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call

object ApiLogger {


    private val MEDIA_TYPE_TEXT = MediaType.get("text/plain")

    fun saveLog(requestBody: String) {
        val apiService = ApiClient.getClient()
        val call = apiService.sendLogs(RequestBody.create(MEDIA_TYPE_TEXT, requestBody))
        Thread{
            proceed(call)
        }.start()
    }

    fun saveDump(requestBody: String) {
        val apiService = ApiClient.getClient()
        val call = apiService.sendDump(RequestBody.create(MEDIA_TYPE_TEXT, requestBody))
        Thread{
            proceed(call)
        }.start()
    }

    private fun proceed(call: Call<ResponseBody>) {
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                Log.i("api.logger()", "success")
            } else {
                Log.e("api.logger", "error. code: ${response.code()}. url: ${call.request().url()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
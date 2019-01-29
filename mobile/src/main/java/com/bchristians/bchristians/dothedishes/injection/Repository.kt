package com.bchristians.bchristians.dothedishes.injection

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.os.AsyncTask
import android.util.Log
import com.bchristians.bchristians.dothedishes.injection.responses.ErrorResponse
import com.bchristians.bchristians.dothedishes.injection.responses.WebResponsePayload
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import okhttp3.OkHttpClient

class Repository {

    private val gson by lazy {
        GsonBuilder().setDateFormat("dd/MM/yyyy").create()
    }

    private val okHttpClient by lazy {
        OkHttpClient()
    }

    fun makeRequest(request: WebRequest): LiveData<WebResponsePayload> {
        val thisCallLiveData: MutableLiveData<WebResponsePayload> = MutableLiveData()

        AsyncTask.execute {
            val response = okHttpClient.newCall(request.buildOkHttpRequest()).execute()
            val rawResponse = response.body()?.string()
            try {
                // If success
                if (response.code() in (200..299)) {
                    val parsedResponse = gson.fromJson(rawResponse, request.responseClass)
                    thisCallLiveData.postValue(parsedResponse)
                } else {
                    val parsedResponse = gson.fromJson(rawResponse, ErrorResponse::class.java)
                    Log.e("Repository", "Web request failed (${response.code()}) with error: \"${parsedResponse.error}\"")
                }
            } catch (e: JsonSyntaxException) {
                Log.e("JSONError", "Failure to parse JSON object as ${request.responseClass}: $rawResponse")
            }
        }

        return thisCallLiveData
    }
}
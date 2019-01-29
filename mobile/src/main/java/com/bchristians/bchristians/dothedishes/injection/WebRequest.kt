package com.bchristians.bchristians.dothedishes.injection

import com.bchristians.bchristians.dothedishes.injection.responses.WebResponsePayload
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody


/**
 * This class serves as an intermediary between the OkHttp3 Request class and the client
 */
class WebRequest(
    private val endpoint: WebRequestEndPoint,
    val responseClass: Class<out WebResponsePayload>,
    val body: Any? = null
) {

    @Suppress("PrivatePropertyName")
    private val BASE_URL = "https://do-the-dishes-service.herokuapp.com/"

    private val gson = Gson()

    fun buildOkHttpRequest(): Request {
        // If more builder functions are added, they must also be implemented here
        val builder = Request.Builder()
            .url("$BASE_URL${this.endpoint.endpoint}")
        if( this.body != null ) {
            builder.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(body).toString()))
        }
        return builder.build()
    }

}
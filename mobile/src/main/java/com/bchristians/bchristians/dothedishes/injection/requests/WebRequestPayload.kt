package com.bchristians.bchristians.dothedishes.injection.requests

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink

class WebRequestPayload: RequestBody() {
    override fun contentType(): MediaType? {
        return MediaType.get("application/json")
    }

    override fun writeTo(sink: BufferedSink) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
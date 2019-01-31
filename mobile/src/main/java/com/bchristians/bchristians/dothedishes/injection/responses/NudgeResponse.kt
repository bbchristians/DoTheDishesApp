package com.bchristians.bchristians.dothedishes.injection.responses

import com.google.gson.annotations.SerializedName

class NudgeResponse(
    val pushStatus: PushStatus? = null
): WebResponsePayload

enum class PushStatus{
    @SerializedName("completed")
    COMPLETED,
    @SerializedName("nudged")
    NUDGED
}
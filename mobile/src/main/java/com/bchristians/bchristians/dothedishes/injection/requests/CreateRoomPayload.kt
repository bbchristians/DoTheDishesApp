package com.bchristians.bchristians.dothedishes.injection.requests

import com.google.gson.annotations.SerializedName

class CreateRoomPayload(
    @SerializedName("roomName")
    val roomName: String
)
package com.bchristians.bchristians.dothedishes.injection.requests

import com.google.gson.annotations.SerializedName


class RegisterUserPayload(
    val userId: String,
    val roomId: Int
)
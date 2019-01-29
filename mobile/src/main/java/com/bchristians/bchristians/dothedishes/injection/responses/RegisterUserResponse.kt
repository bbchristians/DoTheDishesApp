package com.bchristians.bchristians.dothedishes.injection.responses

import com.bchristians.bchristians.dothedishes.user.UserInfo

class RegisterUserResponse(
    val registration: UserInfo? = null,
    val newRegistration: Boolean? = null
): WebResponsePayload
package com.bchristians.bchristians.dothedishes.injection.responses

import com.bchristians.bchristians.dothedishes.room.assignment.Assignment
import com.bchristians.bchristians.dothedishes.user.UserInfo

data class Room(
    val roomId: String?,
    val roomName: String?,
    val registeredUsers: List<UserInfo>?,
    val assignments: List<Assignment>?
): WebResponsePayload
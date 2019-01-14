package com.bchristians.bchristians.dothedishes.room.assignment

import java.util.*

class Assignment(
    val name: String?,
    val date: Date?,
    val completed: Boolean = false,
    val assignedUserId: String?,
    val assignedRoomId: String?,
    val createdUserId: String?
)
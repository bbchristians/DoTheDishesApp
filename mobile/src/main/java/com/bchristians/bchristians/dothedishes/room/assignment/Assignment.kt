package com.bchristians.bchristians.dothedishes.room.assignment

import com.google.gson.annotations.SerializedName
import java.util.*

class Assignment(
    @SerializedName("assignmentName")
    val name: String?,
    val date: Date?,
    val completed: Boolean = false,
    val assignedUser: String?,
    val roomId: Int?,
    val createdUser: String?,
    val assignmentId: Int? = null
)
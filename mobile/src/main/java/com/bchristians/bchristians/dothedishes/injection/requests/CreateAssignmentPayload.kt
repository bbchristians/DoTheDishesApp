package com.bchristians.bchristians.dothedishes.injection.requests

import java.util.*

class CreateAssignmentPayload(
    val roomId: Int,
    val createdUser: String,
    val assignments: List<PayloadAssignment>
)

class PayloadAssignment(
    val assignedUser: String,
    val name: String,
    val date: Date
)
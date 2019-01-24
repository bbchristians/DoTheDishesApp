package com.bchristians.bchristians.dothedishes.injection.requests

import java.util.*

class CreateAssignmentPayload(
    val roomId: Integer,
    val createdUser: String,
    val assignments: List<PayloadAssignment>
)

class PayloadAssignment(
    assignedUser: String,
    name: String,
    date: Date
)
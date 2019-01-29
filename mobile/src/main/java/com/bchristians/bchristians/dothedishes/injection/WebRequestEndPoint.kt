package com.bchristians.bchristians.dothedishes.injection

enum class WebRequestEndPoint(
    val endpoint: String,
    val method: String
){
    CREATE_ROOM("create_room", "POST"),
    GET_ROOM("get_room", "GET"),
    CREATE_ASSIGNMENT("create_assignment", "POST"),
    REGISTER_USER("register_user", "POST"),
    DELETE_ASSIGNMENT("delete_assignment", "POST")
}
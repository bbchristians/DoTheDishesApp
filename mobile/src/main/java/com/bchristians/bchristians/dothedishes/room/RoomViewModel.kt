package com.bchristians.bchristians.dothedishes.room

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.bchristians.bchristians.dothedishes.injection.Repository
import com.bchristians.bchristians.dothedishes.injection.responses.Room
import com.bchristians.bchristians.dothedishes.room.assignment.Assignment
import com.bchristians.bchristians.dothedishes.user.UserInfo
import java.util.*
import javax.inject.Inject

class RoomViewModel @Inject constructor(private val repository: Repository) {

    private val roomLiveData: HashMap<String, MutableLiveData<Room>> = hashMapOf()

    fun getRoomLiveData(roomId: String): LiveData<Room> {
        if( this.roomLiveData.containsKey(roomId) ) {
            makeRoomLiveDataRequest(roomId)
            return this.roomLiveData[roomId] as LiveData<Room>
        }
        this.roomLiveData[roomId] = MutableLiveData()
        makeRoomLiveDataRequest(roomId)
        return this.roomLiveData[roomId] as LiveData<Room>
    }

    fun makeRoomLiveDataRequest(roomId: String) {
        roomLiveData[roomId]?.postValue(
            Room(
                "12345",
                "The Good Place",
                listOf(
                    UserInfo("Kyle", "12345"),
                    UserInfo("Oscar", "12345"),
                    UserInfo("Mehryaar", "12345"),
                    UserInfo("Ben", "12345")
                ),
                listOf(
                    Assignment("Do the Dishes", Date(), false, "Kyle", "12345", "Kyle")
                )
            )
        )
    }
}
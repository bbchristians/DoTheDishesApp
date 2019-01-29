package com.bchristians.bchristians.dothedishes.room

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.bchristians.bchristians.dothedishes.injection.Repository
import com.bchristians.bchristians.dothedishes.injection.WebRequest
import com.bchristians.bchristians.dothedishes.injection.WebRequestEndPoint
import com.bchristians.bchristians.dothedishes.injection.requests.CreateRoomPayload
import com.bchristians.bchristians.dothedishes.injection.responses.Room
import com.bchristians.bchristians.dothedishes.injection.responses.RoomIdResponse
import com.bchristians.bchristians.dothedishes.injection.responses.WebResponsePayload
import com.bchristians.bchristians.dothedishes.room.assignment.Assignment
import com.bchristians.bchristians.dothedishes.room.assignment.AssignmentRepetition
import com.bchristians.bchristians.dothedishes.room.assignment.ScheduleAvailabilityView
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

    fun createAndPostAssignmentCreationEvent(
        roomId: String, assignmentName: String, frequency: AssignmentRepetition,
        createdUserId: String, userAvailability: Map<String, List<ScheduleAvailabilityView.DayOfTheWeek>>
    ) {
        val roomData = this.roomLiveData[roomId]?.value ?: return
        // Shuffle the list of users to actually randomize the list
        val shuffledUsers = userAvailability.keys.shuffled()

        // Generate schedule
        val daysBetween = daysBetween(frequency.startDate, frequency.endDate)
        val userAssignments = HashMap<String, MutableList<Assignment>>()
        shuffledUsers.forEach {
            userAssignments[it] = mutableListOf()
        }
        var curDay = 0
        while( curDay <= daysBetween ) {
            val day = ScheduleAvailabilityView.DayOfTheWeek.values()[curDay % 7]
            var assigneeFound = false
            shuffledUsers.sortedBy { (userAssignments[it]?.size ?: -1) }.forEach { userId ->
                if( !assigneeFound && day in userAvailability[userId] ?: listOf() ) {
                    userAssignments[userId]?.add(
                        Assignment(
                            assignmentName,
                            addDays(frequency.startDate, curDay),
                            false,
                            userId,
                            roomData.roomId,
                            createdUserId
                        )
                    )
                    assigneeFound = true
                    // TODO report if no task added
                }
            }
            curDay++
        }
    }

    fun createRoom(roomName: String): LiveData<WebResponsePayload> {
        val requestLiveData = repository.makeRequest(
            WebRequest(
                WebRequestEndPoint.CREATE_ROOM,
                RoomIdResponse::class.java,
                CreateRoomPayload(roomName)
            )
        )
        return requestLiveData
    }

    companion object {

        fun daysBetween(d1: Date, d2: Date): Long {
            return ((d2.time - d1.time) / (1000L * 60L * 60L * 24L))
        }

        fun addDays(date: Date, days: Int): Date {
            val cal = Calendar.getInstance()
            cal.time = date
            cal.add(Calendar.DAY_OF_YEAR, days)
            return cal.time
        }

    }
}
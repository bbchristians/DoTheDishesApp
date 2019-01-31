package com.bchristians.bchristians.dothedishes.room

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.bchristians.bchristians.dothedishes.injection.Repository
import com.bchristians.bchristians.dothedishes.injection.WebRequest
import com.bchristians.bchristians.dothedishes.injection.WebRequestEndPoint
import com.bchristians.bchristians.dothedishes.injection.requests.*
import com.bchristians.bchristians.dothedishes.injection.responses.*
import com.bchristians.bchristians.dothedishes.room.assignment.Assignment
import com.bchristians.bchristians.dothedishes.room.assignment.AssignmentRepetition
import com.bchristians.bchristians.dothedishes.room.assignment.ScheduleAvailabilityView
import java.util.*
import javax.inject.Inject
import android.arch.lifecycle.Observer
import android.os.Handler


class RoomViewModel @Inject constructor(private val repository: Repository) {

    private val roomLiveData: HashMap<Int, MutableLiveData<Room>> = hashMapOf()

    fun checkRoomExists(roomId: Int): LiveData<Boolean> {
        val existsLiveData = MutableLiveData<Boolean>()
        val roomLiveData = getRoomLiveData(roomId)
        roomLiveData.observeForever(object: Observer<Room> {
            override fun onChanged(t: Room?) {
                existsLiveData.postValue(true)
                roomLiveData.removeObserver(this)
            }
        })
        // Set timeout
        Handler().postDelayed({
            if( existsLiveData.value != true ) {
                existsLiveData.postValue(false)
            }
        }, TIMEOUT_MS)

        return existsLiveData
    }

    fun getRoomLiveData(roomId: Int): LiveData<Room> {
        if( this.roomLiveData.containsKey(roomId) ) {
            makeRoomLiveDataRequest(roomId)
            return this.roomLiveData[roomId] as LiveData<Room>
        }
        this.roomLiveData[roomId] = MutableLiveData()
        makeRoomLiveDataRequest(roomId)
        return this.roomLiveData[roomId] as LiveData<Room>
    }

    fun makeRoomLiveDataRequest(roomId: Int) {
        this.repository.makeRequest(
            WebRequest(
                WebRequestEndPoint.GET_ROOM,
                Room::class.java,
                GetRoomPayload(roomId)
            )
        ).observeForever { responsePayload ->
            if( responsePayload is Room )
                roomLiveData[roomId]?.postValue(responsePayload)
        }
    }

    fun registerUser(roomId: Int, userId: String) {
        repository.makeRequest(
            WebRequest(
                WebRequestEndPoint.REGISTER_USER,
                RegisterUserResponse::class.java,
                RegisterUserPayload(
                    userId, roomId
                )
            )
        )
    }

    fun createAndPostAssignmentCreationEvent(
        roomId: Int, assignmentName: String, frequency: AssignmentRepetition,
        createdUserId: String, userAvailability: Map<String, List<ScheduleAvailabilityView.DayOfTheWeek>>
    ): LiveData<Boolean>? {
        val roomData = this.roomLiveData[roomId]?.value ?: return null
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
            curDay += frequency.frequency
        }

        // Get alert field
        val alertBoolLiveData = MutableLiveData<Boolean>()

        val allNewAssignments = userAssignments.values.flatMap{ value ->
            value.map{ assignment ->
                PayloadAssignment(
                    assignment.assignedUser ?: return null,
                    assignment.name ?: return null,
                    assignment.date ?: return null
                )
            }
        }

        // Post requests to endpoint
        repository.makeRequest(
            WebRequest(
                WebRequestEndPoint.CREATE_ASSIGNMENT,
                AssignmentIdsResponse::class.java,
                CreateAssignmentPayload(
                    roomId,
                    createdUserId,
                    allNewAssignments
                )
            )
        ).observeForever {
            makeRoomLiveDataRequest(roomId)
            alertBoolLiveData.postValue(true)
        }

        return alertBoolLiveData
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

    fun deleteAssignment(assignmentId: Int): LiveData<WebResponsePayload> {
        return repository.makeRequest(
            WebRequest(
                WebRequestEndPoint.DELETE_ASSIGNMENT,
                DeleteAssignmentResponse::class.java,
                DeleteAssignmentPayload(
                    assignmentId
                )
            )
        )
    }

    companion object {

        private const val TIMEOUT_MS = 15000L

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
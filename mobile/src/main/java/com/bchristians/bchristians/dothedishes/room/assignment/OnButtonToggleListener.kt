package com.bchristians.bchristians.dothedishes.room.assignment


abstract class OnButtonToggleListener {

    abstract fun onToggle(dotw: ScheduleAvailabilityView.DayOfTheWeek, toggleValue: Boolean, toggleUser: String)
}
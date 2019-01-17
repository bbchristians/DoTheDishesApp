package com.bchristians.bchristians.dothedishes.room.assignment

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.ToggleButton
import com.bchristians.bchristians.dothedishes.R

class ScheduleAvailabilityView(c: Context, a: AttributeSet): LinearLayout(c,a) {

    var onButtonToggleListener: OnButtonToggleListener? = null
    private var userId: String = ""

    fun assignUser(userId: String) {
        this.userId = userId
        this.findViewById<TextView>(R.id.user_name)?.text = userId
    }

    fun userIsAvailable(dotw: DayOfTheWeek): Boolean {
        return this.findViewById<ToggleButton>(dotw.buttonId)?.isChecked ?: false
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        DayOfTheWeek.values().forEach { dotw ->
            val dotwView = this.findViewById<ToggleButton>(dotw.buttonId)
            dotwView.setOnCheckedChangeListener { _, isChecked ->
                this.onButtonToggleListener?.onToggle(dotw, isChecked, this.userId)
            }
        }
    }

    enum class DayOfTheWeek(val buttonId: Int) {
        SUNDAY(R.id.weekday_availability_sunday),
        MONDAY(R.id.weekday_availability_monday),
        TUESDAY(R.id.weekday_availability_tuesday),
        WEDNESDAY(R.id.weekday_availability_wednesday),
        THURSDAY(R.id.weekday_availability_thursday),
        FRIDAY(R.id.weekday_availability_friday),
        SATURDAY(R.id.weekday_availability_saturday)
    }
}
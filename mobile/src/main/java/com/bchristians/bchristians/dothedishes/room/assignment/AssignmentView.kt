package com.bchristians.bchristians.dothedishes.room.assignment

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.bchristians.bchristians.dothedishes.R
import com.bchristians.bchristians.dothedishes.room.RoomViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AssignmentView(c: Context, a: AttributeSet): LinearLayout(c,a) {

    val dateFormat = SimpleDateFormat("MMM d", Locale.US)

    fun setAssignment(userId: String, assignment: Assignment) {
        // Text fields
        this.findViewById<TextView>(R.id.assignment_title)?.text = assignment.name
        this.findViewById<TextView>(R.id.assignment_date)?.text = formatDate(assignment.date)
        // User dependent fields
        if( userId == assignment.assignedUser ) {
            this.findViewById<TextView>(R.id.action_button)?.text = this.context.getString(R.string.action_button_complete)
        } else {
            this.findViewById<TextView>(R.id.action_button)?.text = this.context.getString(R.string.action_button_nudge)
            // Hide nudge if more than 1 day out
            if( assignment.date != null && RoomViewModel.daysBetween(Calendar.getInstance().time, assignment.date) > 0 ) {
                this.findViewById<TextView>(R.id.action_button)?.visibility = View.GONE
            }
            this.findViewById<TextView>(R.id.assignment_user)?.text = userId
        }
    }

    private fun formatDate(date: Date?): String {
        date ?: return "Unknown"
        val daysUntil = RoomViewModel.daysBetween(Calendar.getInstance().time, date)
        return if( daysUntil < -1 ) {
            "Overdue"
        } else if( -1 <= daysUntil && daysUntil < 0 ) {
            "Today"
        } else if( 0 <= daysUntil && daysUntil < 1) {
            "Tomorrow"
        } else if( 1 <= daysUntil && daysUntil <= 7 ) {
            "${dateFormat.format(date)} (${daysUntil + 1} days)"
        } else {
            dateFormat.format(date)
        }

    }
}
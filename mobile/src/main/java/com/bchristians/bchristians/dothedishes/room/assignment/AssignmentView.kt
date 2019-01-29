package com.bchristians.bchristians.dothedishes.room.assignment

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.bchristians.bchristians.dothedishes.R
import java.util.*

class AssignmentView(c: Context, a: AttributeSet): LinearLayout(c,a) {

    fun setAssignment(userId: String, assignment: Assignment) {
        // Text fields
        this.findViewById<TextView>(R.id.assignment_title)?.text = assignment.name
        this.findViewById<TextView>(R.id.assignment_date)?.text = formatDate(assignment.date)
        // Button
        if( userId == assignment.assignedUser ) {
            this.findViewById<TextView>(R.id.action_button)?.text = this.context.getString(R.string.action_button_complete)
        } else {
            this.findViewById<TextView>(R.id.action_button)?.text = this.context.getString(R.string.action_button_nudge)
        }
    }

    private fun formatDate(date: Date?): String {
        return "Tomorrow"// TODO
    }
}
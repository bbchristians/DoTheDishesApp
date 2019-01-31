package com.bchristians.bchristians.dothedishes.room.assignment

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.bchristians.bchristians.dothedishes.DishesApplication
import com.bchristians.bchristians.dothedishes.R
import com.bchristians.bchristians.dothedishes.room.RoomViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AssignmentView(c: Context, a: AttributeSet): LinearLayout(c,a) {

    val dateFormat = SimpleDateFormat("MMM d", Locale.US)

    private var assignmentId: Int? = null
    private var deleted = false
    private var actionButtonEnabled = false

    @Inject
    lateinit var roomViewModel: RoomViewModel

    fun setAssignment(userId: String, assignment: Assignment) {
        DishesApplication.getApplication().injectorComponent.inject(this)
        this.assignmentId = assignment.assignmentId
        // Text fields
        this.findViewById<TextView>(R.id.assignment_title)?.text = assignment.name
        this.findViewById<TextView>(R.id.assignment_date)?.text = formatDate(assignment.date)
        // User dependent fields
        if( userId == assignment.assignedUser ) {
            this.findViewById<TextView>(R.id.action_button)?.text = this.context.getString(R.string.action_button_complete)
            this.findViewById<TextView>(R.id.assignment_user)?.text = this.context.getString(R.string.assignment_assigned_you)
            setCompleteButtonOnClick()
        } else {
            this.findViewById<TextView>(R.id.action_button)?.text = this.context.getString(R.string.action_button_nudge)
            // Hide nudge if more than 1 day out
            if( assignment.date != null && RoomViewModel.daysBetween(Calendar.getInstance().time, assignment.date) > 0 ) {
                this.findViewById<TextView>(R.id.action_button)?.visibility = View.GONE
            } else {
                setNudgeButtonOnClick()
            }
            this.findViewById<TextView>(R.id.assignment_user)?.text = assignment.assignedUser
        }
        // Delete button
        setDeleteButtonOnClick()
    }

    fun setDeleteMode(enabled: Boolean) {
        if( enabled ) {
            this.findViewById<Button>(R.id.delete_button)?.visibility = View.VISIBLE
            this.findViewById<Button>(R.id.action_button)?.visibility = View.GONE
        } else {
            this.findViewById<Button>(R.id.delete_button)?.visibility = View.GONE
            this.findViewById<Button>(R.id.action_button)?.visibility =
                    if( this.actionButtonEnabled ) View.VISIBLE
                    else View.GONE
        }
    }

    private fun setNudgeButtonOnClick() {
        this.findViewById<Button>(R.id.action_button)?.setOnClickListener {
            roomViewModel.sendNudge(this.assignmentId ?: return@setOnClickListener)
        }
        this.actionButtonEnabled = true
    }

    private fun setCompleteButtonOnClick() {
        // TODO
        this.actionButtonEnabled = true
    }

    private fun setDeleteButtonOnClick() {
        this.assignmentId?.let { assignmentId ->
            this.findViewById<Button>(R.id.delete_button)?.setOnClickListener {
                if( !this.deleted ) {
                    this.deleted = true
                    this.roomViewModel.deleteAssignment(assignmentId).observeForever {
                        this.visibility = View.GONE
                    }
                }
            }
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
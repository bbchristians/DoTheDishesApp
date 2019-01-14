package com.bchristians.bchristians.dothedishes.room

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import com.bchristians.bchristians.dothedishes.R
import com.bchristians.bchristians.dothedishes.room.assignment.Assignment
import com.bchristians.bchristians.dothedishes.room.assignment.AssignmentView
import com.bchristians.bchristians.dothedishes.user.UserInfo
import java.util.*

class RoomFragment: Fragment() {

    private var rootView: View? = null
    private var userInfo: UserInfo? = null
    private var inflater: LayoutInflater? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_room, container, false)
        this.inflater = inflater

        this.userInfo = this.arguments?.getParcelable(this.context?.getString(R.string.user_info_id_key))

        rootView?.let {
            initTitleText(it)
            initAssignmentButtons(it)
        }

        return rootView
    }

    override fun onResume() {
        super.onResume()

        // TODO load assignments from webservice
        val upcomingAssignments = listOf(
            Assignment("Do the Dishes", Date(), false, "Kyle", "12345", "Kyle")
        )

        rootView?.findViewById<ViewGroup>(R.id.upcoming_assignments_holder)?.let { upcomingAssignmentsHolder ->
            upcomingAssignmentsHolder.removeAllViews()
            upcomingAssignments.forEach { assignment ->
                val newAssignmentView = inflater?.inflate(R.layout.view_assignment, upcomingAssignmentsHolder, false) as? AssignmentView
                newAssignmentView?.setAssignment(this.userInfo?.userId ?: return, assignment)
                upcomingAssignmentsHolder.addView(newAssignmentView)
            }
        }
    }

    private fun initTitleText(rootView: View) {
        // Room Welcome
        rootView.findViewById<TextView>(R.id.room_welcome)?.text =
                context?.getString(R.string.welcome_text, this.userInfo?.userId ?: "Unknown")
        // Room Name
        rootView.findViewById<TextView>(R.id.room_name)?.text = "The Good Place" // TODO load from webservice
        // Room Id Label
        rootView.findViewById<TextView>(R.id.room_id)?.text =
                context?.getString(R.string.room_id, this.userInfo?.roomId ?: "Unknown")
    }

    private fun initAssignmentButtons(rootView: View) {
        rootView.findViewById<Button>(R.id.new_assignment_button)?.setOnClickListener {
            (this.activity as? RoomActivity)?.startCreateAssignmentActivity()
        }

        rootView.findViewById<ToggleButton>(R.id.delete_mode_button)?.let { deleteModeButton ->
            // TODO make this button work
        }
    }
}
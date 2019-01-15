package com.bchristians.bchristians.dothedishes.room

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import com.bchristians.bchristians.dothedishes.DishesApplication
import com.bchristians.bchristians.dothedishes.R
import com.bchristians.bchristians.dothedishes.injection.responses.Room
import com.bchristians.bchristians.dothedishes.room.assignment.Assignment
import com.bchristians.bchristians.dothedishes.room.assignment.AssignmentView
import com.bchristians.bchristians.dothedishes.user.UserInfo
import javax.inject.Inject

class RoomFragment: Fragment(), Observer<Room> {

    private var rootView: View? = null
    private var userInfo: UserInfo? = null
    private var inflater: LayoutInflater? = null

    @Inject
    lateinit var roomViewModel: RoomViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_room, container, false)
        this.inflater = inflater

        DishesApplication.getApplication().injectorComponent.inject(this)

        this.userInfo = this.arguments?.getParcelable(this.context?.getString(R.string.user_info_id_key))

        this.userInfo?.roomId?.let {
            roomViewModel.getRoomLiveData(it).observe(this,this)
        }

        rootView?.let {
            initAssignmentButtons(it)
            initTitleText()
        }

        return rootView
    }

    private fun initTitleText() {
        // Room Welcome
        this.rootView?.findViewById<TextView>(R.id.room_welcome)?.text =
                this.context?.getString(R.string.welcome_text, this.userInfo?.userId ?: "Unknown")
        // Room Name placeholder text
        this.rootView?.findViewById<TextView>(R.id.room_name)?.text =
                this.context?.getString(R.string.room_name_placeholder) ?: "Unknown"
        // Room Id Label
        this.rootView?.findViewById<TextView>(R.id.room_id)?.text =
                this.context?.getString(R.string.room_id, this.userInfo?.roomId ?: "Unknown")
    }

    private fun initAssignmentButtons(rootView: View) {
        rootView.findViewById<Button>(R.id.new_assignment_button)?.setOnClickListener {
            (this.activity as? RoomActivity)?.startCreateAssignmentActivity()
        }

        rootView.findViewById<ToggleButton>(R.id.delete_mode_button)?.let { deleteModeButton ->
            // TODO make this button work
        }
    }

    private fun displayAssignments(assignments: List<Assignment>) {
        rootView?.findViewById<ViewGroup>(R.id.upcoming_assignments_holder)?.let { upcomingAssignmentsHolder ->
            upcomingAssignmentsHolder.removeAllViews()
            assignments.forEach { assignment ->
                val newAssignmentView = inflater?.inflate(R.layout.view_assignment, upcomingAssignmentsHolder, false) as? AssignmentView
                newAssignmentView?.setAssignment(this.userInfo?.userId ?: return, assignment)
                upcomingAssignmentsHolder.addView(newAssignmentView)
            }
        }
    }

    private fun updateTitleInfo(room: Room) {
        this.rootView?.findViewById<TextView>(R.id.room_name)?.text = room.roomName
    }

    override fun onChanged(t: Room?) {
        t ?: return
        initTitleText()
        displayAssignments(t.assignments ?: return)
        updateTitleInfo(t)
    }
}
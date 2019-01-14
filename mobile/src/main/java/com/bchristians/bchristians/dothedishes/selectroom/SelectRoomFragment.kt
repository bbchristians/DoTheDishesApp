package com.bchristians.bchristians.dothedishes.selectroom

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.bchristians.bchristians.dothedishes.MainActivity
import com.bchristians.bchristians.dothedishes.R

class SelectRoomFragment: Fragment() {

    private var rootView: View? = null
    private var inflater: LayoutInflater? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.rootView = inflater.inflate(R.layout.fragment_select_room, container, false)
        this.inflater = inflater
        this.rootView?.let {
            initSubmitButton(it)
        }

        return this.rootView
    }

    override fun onResume() {
        super.onResume()
        this.rootView?.let {
            initPastRoomList(it, inflater)
        }
    }

    private fun initSubmitButton(root: View) {
        root.findViewById<Button>(R.id.submit_button)?.let { button ->
            button.setOnClickListener {
                val enteredRoomId = root.findViewById<EditText>(R.id.room_id_edit_text)?.text?.toString() ?: ""
                if ( enteredRoomId.isNotEmpty() ) {
                    (this.context as? MainActivity)?.submitRoomId(enteredRoomId)
                } else {
                    // TODO error report
                }
            }
        }
    }

    private fun initPastRoomList(root: View, inflater: LayoutInflater?) {
        root.findViewById<ViewGroup>(R.id.previous_room_ids)?.let { previousRoomsHolder ->
            val pastRooms = (this.context as? MainActivity)?.getAllAccessedRooms() ?: mutableListOf()
            if( pastRooms.isNotEmpty() ) {
                previousRoomsHolder.removeAllViews()
                pastRooms.forEach { roomId ->
                    val newPastRoom = inflater?.inflate(R.layout.view_previous_room, previousRoomsHolder, false) as? PreviousRoomView
                    newPastRoom?.roomId = roomId
                    previousRoomsHolder.addView(newPastRoom)
                }
            }
        }
    }
}
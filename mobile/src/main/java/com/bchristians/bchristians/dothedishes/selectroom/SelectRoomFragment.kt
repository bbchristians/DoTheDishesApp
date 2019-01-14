package com.bchristians.bchristians.dothedishes.selectroom

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.bchristians.bchristians.dothedishes.R
import com.bchristians.bchristians.dothedishes.room.RoomActivity

class SelectRoomFragment: Fragment() {

    private var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_select_room, container, false)

        rootView?.let {
            initSubmitButton(it)
        }

        return rootView
    }

    private fun initSubmitButton(root: View) {
        root.findViewById<Button>(R.id.submit_button)?.let { button ->
            button.setOnClickListener {
                val enteredRoomId = root.findViewById<EditText>(R.id.room_id_edit_text)?.text?.toString() ?: ""
                if ( enteredRoomId.isNotEmpty() ) {
                    submitRoomId(enteredRoomId)
                } else {
                    // TODO error report
                }
            }
        }
    }

    private fun submitRoomId(roomId: String) {
        val roomIntent = Intent(this.context, RoomActivity::class.java)
        roomIntent.putExtra(this.context?.getString(R.string.room_id_key), roomId)
        this.context?.startActivity(roomIntent)
    }
}
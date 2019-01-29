package com.bchristians.bchristians.dothedishes.createroom

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.bchristians.bchristians.dothedishes.DishesApplication
import com.bchristians.bchristians.dothedishes.R
import com.bchristians.bchristians.dothedishes.injection.responses.RoomIdResponse
import com.bchristians.bchristians.dothedishes.room.RoomViewModel
import com.bchristians.bchristians.dothedishes.user.UserInfo
import javax.inject.Inject

class CreateRoomFragment: Fragment() {

    @Inject
    lateinit var roomViewModel: RoomViewModel

    private var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_create_room, container, false)

        DishesApplication.getApplication().injectorComponent.inject(this)

        rootView?.let {
            setupConfirmButton(it)
        }

        return rootView
    }

    private fun setupConfirmButton(root: View) {
        root.findViewById<Button>(R.id.confirm_button)?.setOnClickListener {
            val enteredName = root.findViewById<EditText>(R.id.field_room_name)?.text?.toString()
            val enteredUserId = root.findViewById<EditText>(R.id.user_id_edit_text)?.text?.toString()
            if( enteredName == null ) {
                // TODO report error
                return@setOnClickListener
            }
            if( enteredUserId == null ) {
                // TODO report error
                return@setOnClickListener
            }
            roomViewModel.createRoom(enteredName).observeForever { responsePayload ->
                if( responsePayload is RoomIdResponse ) {
                    (this.activity as? CreateRoomActivity)?.submitRoomId(
                        UserInfo(
                            enteredUserId,
                            responsePayload.roomId ?: return@observeForever
                        )
                    )
                }
            }
        }
    }

}
package com.bchristians.bchristians.dothedishes.room

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bchristians.bchristians.dothedishes.R
import com.bchristians.bchristians.dothedishes.user.UserInfo

class RoomFragment: Fragment() {

    private var rootView: View? = null
    private var userInfo: UserInfo? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_room, container, false)

        this.userInfo = this.arguments?.getParcelable(this.context?.getString(R.string.user_info_id_key))

        rootView?.let {
            initTitleText(it)
        }

        return rootView
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
}
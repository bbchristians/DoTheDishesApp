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
            initTitle(it)
        }

        return rootView
    }

    private fun initTitle(rootView: View) {
        val titleText = "${userInfo?.userId}: ${userInfo?.roomId}"
        rootView.findViewById<TextView>(R.id.room_title)?.text = titleText
    }
}
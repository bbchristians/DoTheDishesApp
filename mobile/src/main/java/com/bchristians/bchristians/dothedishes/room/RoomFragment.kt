package com.bchristians.bchristians.dothedishes.room

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bchristians.bchristians.dothedishes.R

class RoomFragment: Fragment() {

    private var rootView: View? = null
    private var roomId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_room, container, false)

        this.roomId = this.arguments?.getString(this.context?.getString(R.string.room_id_key))

        rootView?.let {
            initTitle(it)
        }

        return rootView
    }

    private fun initTitle(rootView: View) {
        rootView.findViewById<TextView>(R.id.room_title)?.text = this.roomId
    }
}
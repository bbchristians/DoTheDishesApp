package com.bchristians.bchristians.dothedishes.room

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bchristians.bchristians.dothedishes.R

class RoomFragment: Fragment() {

    private var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_room, container, false)

        return rootView
    }
}
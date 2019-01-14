package com.bchristians.bchristians.dothedishes.room

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.bchristians.bchristians.dothedishes.R

class RoomActivity: AppCompatActivity() {

    private var roomId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_room)

        val roomIdKey = this.getString(R.string.room_id_key)
        this.roomId = this.intent?.extras?.getString(roomIdKey)

        roomId ?: this.finish()

        val fragment = RoomFragment()
        val roomIdBundle = Bundle()
        roomIdBundle.putString(roomIdKey, this.roomId)
        fragment.arguments = roomIdBundle
        switchToFragment(fragment, "room")
    }

    fun switchToFragment(fragment: Fragment, tag: String?) {
        val transaction = supportFragmentManager?.beginTransaction() ?: return
        transaction.replace(R.id.content_frame, fragment)
        transaction.addToBackStack(tag)
        transaction.commit()
    }
}
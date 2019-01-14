package com.bchristians.bchristians.dothedishes

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.bchristians.bchristians.dothedishes.room.RoomActivity
import com.bchristians.bchristians.dothedishes.selectroom.SelectRoomFragment
import com.bchristians.bchristians.dothedishes.sharedpreferences.SharedPreferencesClient

class MainActivity: AppCompatActivity() {

    private lateinit var sharedPreferenceClient: SharedPreferencesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.sharedPreferenceClient = SharedPreferencesClient(this)

        this.setContentView(R.layout.activity_main)

        switchToFragment(SelectRoomFragment(), "select_room")
    }

    fun switchToFragment(fragment: Fragment, tag: String?) {
        val transaction = supportFragmentManager?.beginTransaction() ?: return
        transaction.replace(R.id.content_frame, fragment)
        transaction.addToBackStack(tag)
        transaction.commit()
    }

    fun submitRoomId(roomId: String) {
        // Save the room to persistent storage so that it is known as the last accessed room
        this.sharedPreferenceClient.addAccessedRoom(roomId)
        // Start the new activity for the selected room
        val roomIntent = Intent(this, RoomActivity::class.java)
        roomIntent.putExtra(this.getString(R.string.room_id_key), roomId)
        this.startActivity(roomIntent)
    }

    fun getAllAccessedRooms() = this.sharedPreferenceClient.getAllAccessedRooms()
}
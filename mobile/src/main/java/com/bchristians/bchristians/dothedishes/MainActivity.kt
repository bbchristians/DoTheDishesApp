package com.bchristians.bchristians.dothedishes

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.bchristians.bchristians.dothedishes.createroom.CreateRoomActivity
import com.bchristians.bchristians.dothedishes.room.RoomActivity
import com.bchristians.bchristians.dothedishes.selectroom.SelectRoomFragment
import com.bchristians.bchristians.dothedishes.sharedpreferences.SharedPreferencesClient
import com.bchristians.bchristians.dothedishes.user.UserInfo

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

    fun submitRoomId(userInfo: UserInfo) {
        // Save the room to persistent storage so that it is known as the last accessed room
        this.sharedPreferenceClient.addAccessedRoom(userInfo)
        // Start the new activity for the selected room
        val roomIntent = Intent(this, RoomActivity::class.java)
        roomIntent.putExtra(this.getString(R.string.user_info_id_key), userInfo)
        this.startActivity(roomIntent)
    }

    fun getAllAccessedRooms() = this.sharedPreferenceClient.getAllAccessedRooms()

    fun startCreateRoomActivity() {
        val createRoomIntent = Intent(this, CreateRoomActivity::class.java)
        this.startActivity(createRoomIntent)
    }
}
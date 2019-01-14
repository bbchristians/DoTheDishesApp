package com.bchristians.bchristians.dothedishes.room

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.bchristians.bchristians.dothedishes.R
import com.bchristians.bchristians.dothedishes.user.UserInfo

class RoomActivity: AppCompatActivity() {

    private var userInfo: UserInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_room)

        val userInfoKey = this.getString(R.string.user_info_id_key)
        this.userInfo = this.intent?.extras?.getParcelable(userInfoKey)

        userInfo ?: this.finish()

        val fragment = RoomFragment()
        val roomIdBundle = Bundle()
        roomIdBundle.putParcelable(userInfoKey, this.userInfo)
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
package com.bchristians.bchristians.dothedishes.room.assignment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.Window
import com.bchristians.bchristians.dothedishes.R
import com.bchristians.bchristians.dothedishes.user.UserInfo

class CreateAssignmentActivity: AppCompatActivity() {

    private var userInfo: UserInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userInfoKey = this.getString(R.string.user_info_id_key)
        this.userInfo = this.intent?.extras?.getParcelable(userInfoKey)

        requestWindowFeature(Window.FEATURE_ACTION_BAR)

        setContentView(R.layout.activity_room)

        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)

        val fragment = CreateAssignmentFragment()
        val roomIdBundle = Bundle()
        roomIdBundle.putParcelable(userInfoKey, this.userInfo)
        fragment.arguments = roomIdBundle
        switchToFragment(fragment, "create_assignment")
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            this.finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        this.finish()
        super.onBackPressed()
    }

    fun switchToFragment(fragment: Fragment, tag: String?) {
        val transaction = supportFragmentManager?.beginTransaction() ?: return
        transaction.replace(R.id.content_frame, fragment)
        transaction.addToBackStack(tag)
        transaction.commit()
    }
}

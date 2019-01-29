package com.bchristians.bchristians.dothedishes.createroom

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.MotionEvent
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.bchristians.bchristians.dothedishes.R
import com.bchristians.bchristians.dothedishes.room.RoomActivity
import com.bchristians.bchristians.dothedishes.sharedpreferences.SharedPreferencesClient
import com.bchristians.bchristians.dothedishes.user.UserInfo

class CreateRoomActivity: AppCompatActivity() {

    private lateinit var sharedPreferenceClient: SharedPreferencesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.sharedPreferenceClient = SharedPreferencesClient(this)

        requestWindowFeature(Window.FEATURE_ACTION_BAR)

        setContentView(R.layout.activity_create_room)

        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)

        switchToFragment(CreateRoomFragment(), "create_room")
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

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
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
}
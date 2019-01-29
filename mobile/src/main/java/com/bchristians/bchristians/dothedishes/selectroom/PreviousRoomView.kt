package com.bchristians.bchristians.dothedishes.selectroom

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.bchristians.bchristians.dothedishes.MainActivity
import com.bchristians.bchristians.dothedishes.R
import com.bchristians.bchristians.dothedishes.user.UserInfo

class PreviousRoomView(c: Context, a: AttributeSet): LinearLayout(c, a) {

    var roomId: UserInfo = UserInfo("", -1)
    set (value) {
        field = value
        this.findViewById<TextView>(R.id.room_id)?.text = value.roomId.toString()
        this.findViewById<TextView>(R.id.user_id)?.text = value.userId
        this.findViewById<Button>(R.id.submit_button_prev)?.setOnClickListener {
            (this.context as? MainActivity)?.submitRoomId(value)
        }
    }
}
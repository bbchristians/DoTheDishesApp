package com.bchristians.bchristians.dothedishes.selectroom

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.bchristians.bchristians.dothedishes.MainActivity
import com.bchristians.bchristians.dothedishes.R

class PreviousRoomView(c: Context, a: AttributeSet): LinearLayout(c, a) {

    var roomId: String = ""
    set (value) {
        field = value
        this.findViewById<TextView>(R.id.room_id)?.text = value
        this.findViewById<Button>(R.id.submit_button_prev)?.setOnClickListener {
            (this.context as? MainActivity)?.submitRoomId(value)
        }
    }
}
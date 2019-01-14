package com.bchristians.bchristians.dothedishes

import android.content.Intent
import android.support.v7.app.AppCompatActivity

class LaunchActivity: AppCompatActivity() {

    override fun onStart() {
        super.onStart()

        val mainActivityIntent = Intent(this, MainActivity::class.java)
        this.startActivity(mainActivityIntent)
    }
}
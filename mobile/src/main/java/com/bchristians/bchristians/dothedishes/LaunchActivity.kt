package com.bchristians.bchristians.dothedishes

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.bchristians.bchristians.dothedishes.pushnotifications.FirebaseViewModel
import javax.inject.Inject

class LaunchActivity: AppCompatActivity() {

    @Inject
    lateinit var firebaseViewModel: FirebaseViewModel

    override fun onStart() {
        super.onStart()

        DishesApplication.getApplication().injectorComponent.inject(this)

        firebaseViewModel.refreshFirebaseDeviceId()

        val mainActivityIntent = Intent(this, MainActivity::class.java)
        this.startActivity(mainActivityIntent)
    }
}
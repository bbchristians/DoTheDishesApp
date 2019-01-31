package com.bchristians.bchristians.dothedishes.pushnotifications

import android.util.Log
import com.bchristians.bchristians.dothedishes.injection.Repository
import com.bchristians.bchristians.dothedishes.user.UserInfo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.pusher.pushnotifications.PushNotifications
import javax.inject.Inject

class FirebaseViewModel @Inject constructor(private val repository: Repository) {

    fun refreshFirebaseDeviceId() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("Firebase", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token
                Log.d("FirebaseTokenCreated", token)
            })
    }

    fun setRegistrationSubscription(registration: UserInfo) {
        val interest = "${registration.userId}@${registration.roomId}"
        Log.d("PushNotifications", "Device subscribed to interest: $interest")
        PushNotifications.subscribe(interest)
    }
}


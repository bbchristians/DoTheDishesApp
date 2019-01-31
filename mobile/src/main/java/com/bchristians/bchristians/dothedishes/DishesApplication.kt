package com.bchristians.bchristians.dothedishes

import android.app.Application
import com.bchristians.bchristians.dothedishes.injection.DaggerDishesComponent
import com.bchristians.bchristians.dothedishes.injection.DishesComponent
import com.bchristians.bchristians.dothedishes.injection.DishesInjector
import com.bchristians.bchristians.dothedishes.injection.Repository
import com.google.firebase.FirebaseApp
import com.pusher.pushnotifications.PushNotifications

class DishesApplication: Application() {

    lateinit var injectorComponent: DishesComponent

    override fun onCreate() {
        super.onCreate()

        setApplication(this)

        this.injectorComponent = DaggerDishesComponent.builder()
            .dishesInjector(DishesInjector(Repository()))
            .build()

        // Initialize the firebase client with the app
        FirebaseApp.initializeApp(this)

        PushNotifications.start(this, "fc67890c-2ad8-418d-90a7-b4b845cf03c1");
    }

    companion object {

        private lateinit var application: DishesApplication

        private fun setApplication(app: DishesApplication) {
            this.application = app
        }

        fun getApplication() = application

    }
}
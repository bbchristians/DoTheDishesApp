package com.bchristians.bchristians.dothedishes

import android.app.Application
import com.bchristians.bchristians.dothedishes.injection.DaggerDishesComponent
import com.bchristians.bchristians.dothedishes.injection.DishesComponent
import com.bchristians.bchristians.dothedishes.injection.DishesInjector
import com.bchristians.bchristians.dothedishes.injection.Repository

class DishesApplication: Application() {

    lateinit var injectorComponent: DishesComponent

    override fun onCreate() {
        super.onCreate()

        setApplication(this)

        this.injectorComponent = DaggerDishesComponent.builder()
            .dishesInjector(DishesInjector(Repository()))
            .build()
    }

    companion object {

        private lateinit var application: DishesApplication

        private fun setApplication(app: DishesApplication) {
            this.application = app
        }

        fun getApplication() = application

    }
}
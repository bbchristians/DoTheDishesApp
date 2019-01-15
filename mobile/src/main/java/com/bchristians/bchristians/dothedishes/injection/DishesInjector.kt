package com.bchristians.bchristians.dothedishes.injection

import com.bchristians.bchristians.dothedishes.room.RoomViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DishesInjector(private val repository: Repository) {

    @Provides
    @Singleton
    fun providesRoomViewModel() = RoomViewModel(this.repository)
}
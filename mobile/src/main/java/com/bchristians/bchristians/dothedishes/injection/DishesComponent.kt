package com.bchristians.bchristians.dothedishes.injection

import com.bchristians.bchristians.dothedishes.createroom.CreateRoomFragment
import com.bchristians.bchristians.dothedishes.room.RoomFragment
import com.bchristians.bchristians.dothedishes.room.assignment.CreateAssignmentFragment
import com.bchristians.bchristians.dothedishes.selectroom.SelectRoomFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules=[DishesInjector::class])
interface DishesComponent {

    fun inject(f: RoomFragment)

    fun inject(f: CreateAssignmentFragment)

    fun inject(f: CreateRoomFragment)

    fun inject(f: SelectRoomFragment)

}


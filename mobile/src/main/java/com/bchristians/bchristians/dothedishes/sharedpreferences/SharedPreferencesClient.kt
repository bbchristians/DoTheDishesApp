package com.bchristians.bchristians.dothedishes.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SharedPreferencesClient(context: Context) {

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getAllAccessedRooms(): MutableList<String> {
        val persistedString = sharedPreferences.getString(ACCESSED_ROOMS_KEY, "")
        if( persistedString?.isEmpty() != false ) return mutableListOf()
        return persistedString.split("|^|")?.toMutableList() ?: mutableListOf()
    }

    fun addAccessedRoom(roomId: String) {
        val allAccessedRooms = this.getAllAccessedRooms()
        if( allAccessedRooms.contains(roomId) ) {
            allAccessedRooms.remove(roomId)
        }
        allAccessedRooms.add(0, roomId)
        this.sharedPreferences.edit().putString(ACCESSED_ROOMS_KEY, allAccessedRooms.joinToString("|^|") ).apply()
    }

    companion object {
        private const val ACCESSED_ROOMS_KEY = "ACCESSED_ROOMS"
    }
}
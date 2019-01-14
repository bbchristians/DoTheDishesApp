package com.bchristians.bchristians.dothedishes.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.bchristians.bchristians.dothedishes.user.UserInfo
import com.google.gson.Gson
import com.google.gson.JsonParseException

class SharedPreferencesClient(context: Context) {

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getAllAccessedRooms(): MutableList<UserInfo> {
        val persistedString = sharedPreferences.getString(ACCESSED_ROOMS_KEY, "")
        if( persistedString?.isEmpty() != false ) return mutableListOf()
        val gson = Gson()
        return persistedString.split("|^|").asSequence().map { userInfoString ->
            try {
                gson.fromJson(userInfoString, UserInfo::class.java)
            } catch( e: JsonParseException ) {
                UserInfo("", "")
            }
        }.toMutableList()
    }

    fun addAccessedRoom(userInfo: UserInfo) {
        val allAccessedRooms = this.getAllAccessedRooms()
        if( allAccessedRooms.contains(userInfo) ) {
            allAccessedRooms.remove(userInfo)
        }
        allAccessedRooms.add(0, userInfo)
        val gson = Gson()
        this.sharedPreferences.edit().putString(ACCESSED_ROOMS_KEY, allAccessedRooms.asSequence().map { thisUserInfo ->
            gson.toJson(thisUserInfo)
        }.joinToString("|^|") ).apply()
    }

    companion object {
        private const val ACCESSED_ROOMS_KEY = "ACCESSED_ROOMS"
    }
}
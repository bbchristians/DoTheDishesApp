package com.bchristians.bchristians.dothedishes.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo(
    val userId: String,
    val roomId: String
): Parcelable